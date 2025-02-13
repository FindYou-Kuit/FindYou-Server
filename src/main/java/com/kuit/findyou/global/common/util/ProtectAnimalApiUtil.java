package com.kuit.findyou.global.common.util;

import com.kuit.findyou.global.common.dto.response.ProtectAnimalApiResponse;
import com.kuit.findyou.domain.report.model.Neutering;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.Sex;
import com.kuit.findyou.domain.report.repository.ProtectingReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProtectAnimalApiUtil {

    private final ProtectingReportRepository protectingReportRepository;

    private WebClient webClient;

    @Value("${api.serviceKey}")
    private String serviceKey;

    private static final String BASE_URL = "http://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic";

    public List<ProtectAnimalApiResponse.Item> getAllProtectAnimalApiData(String bgnde, String endde, String state) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BASE_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(600));

        webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .uriBuilderFactory(factory)
                .baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        List<ProtectAnimalApiResponse.Item> allData = new ArrayList<>();
        int pageNo = 1;
        int numOfRows = 1000; // 최대 개수

        while (true) {
            int finalPageNo = pageNo;
            ProtectAnimalApiResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("bgnde", bgnde)
                            .queryParam("endde", endde)
                            .queryParam("pageNo", finalPageNo)
                            .queryParam("numOfRows", numOfRows)
                            .queryParam("state", state)  // protect 아님 notice
                            .queryParam("_type", "json")
                            .build())
                    .header("Accept", "application/json")
                    .retrieve()
                    .bodyToMono(ProtectAnimalApiResponse.class)
                    .block();

            if (response == null || response.getResponse().getBody().getItems().getItem() == null) {
                break; // 데이터가 없을 경우 루프 종료
            }

            allData.addAll(response.getResponse().getBody().getItems().getItem());
            pageNo++; // 다음 페이지 요청
        }

        return allData;
    }

    @Transactional
    public void syncProtectingReports(String bgnde, String endde, String state) {
        // 1. 외부 API에서 데이터 가져오기
        List<ProtectAnimalApiResponse.Item> items = getAllProtectAnimalApiData(bgnde, endde, state);

        // 2. 외부 API에서 받은 모든 noticeNumber 추출
        List<String> incomingNoticeNumbers = items.stream()
                .map(ProtectAnimalApiResponse.Item::getNoticeNo)
                .collect(Collectors.toList());

        // 3. DB에 저장된 기존 ProtectingReport 중 데이터 조회
        List<ProtectingReport> existingReports = protectingReportRepository.findByNoticeNumberIn(incomingNoticeNumbers);

        // 4. 기존 DB에 있지만, 외부 API 데이터에 없는 데이터 삭제 -> 즉 공공 API에서 더이상 관리하지 않는 데이터는 DB에서도 삭제
        List<ProtectingReport> toDelete = existingReports.stream()
                .filter(report -> !incomingNoticeNumbers.contains(report.getNoticeNumber()))
                .collect(Collectors.toList());
        protectingReportRepository.deleteAll(toDelete);

        // 5. 외부 API 데이터 중 DB에 없는 데이터만 필터링 -> 새롭게 추가된 데이터들
        List<ProtectingReport> newReports = items.stream()
                .filter(item -> existingReports.stream()
                        .noneMatch(report -> report.getNoticeNumber().equals(item.getNoticeNo())))
                .map(response -> ProtectingReport.builder()
                        .happenDate(response.changeToLocalDate(response.getHappenDt()))
                        .imageUrl(response.getPopfile())
                        .species(response.extractSpecies())
                        .noticeNumber(response.getNoticeNo())
                        .noticeStartDate(response.changeToLocalDate(response.getNoticeSdt()))
                        .noticeEndDate(response.changeToLocalDate(response.getNoticeEdt()))
                        .breed(response.extractBreed())
                        .furColor(response.extractFurColor())
                        .weight(response.extractWeight())
                        .age(response.extractYear())
                        .sex(Sex.valueOf(response.getSexCd()))
                        .neutering(Neutering.valueOf(response.getNeuterYn()))
                        .foundLocation(response.getHappenPlace())
                        .significant(response.getSpecialMark() == null ? "미등록" : response.getSpecialMark())
                        .careName(response.getCareNm())
                        .careAddr(response.getCareAddr())
                        .careTel(response.getCareTel())
                        .authority(response.getOrgNm())
                        .authorityPhoneNumber(response.getOfficetel())
                        .build())
                .collect(Collectors.toList());

        // 6. 새로운 데이터 저장
        protectingReportRepository.saveAll(newReports);

        log.info("DB 동기화 완료: 삭제된 데이터 = {}, 추가된 데이터 = {}", toDelete.size(), newReports.size());
    }

    @Transactional
    public void updateProtectingReportWithProtectState() {
        // state = protect 인 데이터 획득 및 update
        syncProtectingReports("20220101", getCurrentDate(), "protect");
    }

    @Transactional
    public void updateProtectingReportWithNoticeState() {
        // state = notice 인 데이터 획득 및 update
        syncProtectingReports(calculateStartDate("30"), getCurrentDate(), "notice");
    }

    @Scheduled(cron = "0 30 4 * * ?")    // 4시 30분으로 스케줄링 설정
    @Transactional
    public void updateAllProtectingReports() {
        updateProtectingReportWithNoticeState();
        updateProtectingReportWithProtectState();
    }

    private String calculateStartDate(String recentDays) {
        // 날짜 계산 로직 (예: 최근 7일 등)
        return LocalDate.now().minusDays(Long.parseLong(recentDays)).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private String getCurrentDate() {
        // 오늘 날짜 반환
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}

