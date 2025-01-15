package com.kuit.findyou.domain.report.controller;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.*;
import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.domain.report.repository.*;
import com.kuit.findyou.domain.report.service.*;
import com.kuit.findyou.global.common.response.BaseResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportAnimalInfoService reportAnimalInfoService;
    private final ProtectingAnimalInfoService protectingAnimalInfoService;
    private final ProtectingAnimalRetrieveService protectingAnimalRetrieveService;
    private final ReportAnimalRetrieveService reportAnimalRetrieveService;
    private final AnimalRetrieveService animalRetrieveService;


    // test에 필요한 레포지토리들
    private final UserRepository userRepository;
    private final ProtectingReportRepository protectingReportRepository;
    private final InterestProtectingReportRepository interestProtectingReportRepository;
    private final BreedRepository breedRepository;
    private final AnimalFeatureRepository animalFeatureRepository;
    private final ReportAnimalRepository reportAnimalRepository;
    private final ReportedAnimalFeatureRepository reportedAnimalFeatureRepository;
    private final ReportRepository reportRepository;
    private final InterestReportRepository interestReportRepository;


    @GetMapping("/report-animals/{report_id}")
    public BaseResponse<ReportInfoDTO> reportInfo(@PathVariable("report_id") Long reportId) {
        ReportInfoDTO findReportInfo = reportAnimalInfoService.findReportInfoById(reportId, 1L);

        return new BaseResponse<>(findReportInfo);
    }

    @GetMapping("/protecting-animals/{protecting_report_id}")
    public BaseResponse<ProtectingReportInfoDTO> protectingReportInfo(@PathVariable("protecting_report_id") Long protectingReportId) {
        ProtectingReportInfoDTO findProtectingInfo = protectingAnimalInfoService.findProtectingReportInfoById(protectingReportId, 1L);

        return new BaseResponse<>(findProtectingInfo);
    }

    @GetMapping("/protecting-animals")
    public BaseResponse<ProtectingReportCardDTO> retrieveProtectingReports(@RequestParam("lastProtectId") Long lastProtectId) {
        ProtectingReportCardDTO protectingReportCardDTO = protectingAnimalRetrieveService.retrieveProtectingReportCards(1L, lastProtectId);

        return new BaseResponse<>(protectingReportCardDTO);
    }

    @GetMapping("/report-animals")
    public BaseResponse<ReportCardDTO> retrieveReports(@RequestParam("lastReportId") Long lastReportId) {
        ReportCardDTO reportCardDTO = reportAnimalRetrieveService.retrieveReportCards(1L, lastReportId);

        return new BaseResponse<>(reportCardDTO);
    }

    @GetMapping
    public BaseResponse<TotalCardDTO> retrieveAll(@RequestParam("lastProtectId") Long lastProtectId, @RequestParam("lastReportId") Long lastReportId) {
        TotalCardDTO totalCardDTO = animalRetrieveService.retrieveTotalCards(1L, lastProtectId, lastReportId);

        return new BaseResponse<>(totalCardDTO);
    }

    @PostConstruct
    public void init() {
        User user = User.builder()
                .name("김상균")
                .email("ksg001227@naver.com")
                .password("skcjswo00")
                .build();

        userRepository.save(user);

        //=========================================
        // 품종, 축종 설정
        Breed breed = Breed.builder()
                .name("시츄")
                .species("개")
                .build();
        breedRepository.save(breed);
        //=========================================

        //=========================================
        // 동물 특징 생성
        AnimalFeature animalFeature = AnimalFeature.builder().featureValue("순해요").build();
        AnimalFeature animalFeature2 = AnimalFeature.builder().featureValue("물어요").build();
        animalFeatureRepository.save(animalFeature);
        animalFeatureRepository.save(animalFeature2);
        //=========================================


        for (int i = 1; i <= 41; i++) {
            ProtectingReport protectingReport = ProtectingReport.builder()
                    .happenDate(LocalDate.now())
                    .imageUrl(String.valueOf(i))
                    .species(String.valueOf(i))
                    .noticeNumber(String.valueOf(i))
                    .noticeStartDate(LocalDate.now())
                    .noticeEndDate(LocalDate.now())
                    .breed(String.valueOf(i))
                    .furColor(String.valueOf(i))
                    .weight(3.5F)
                    .age((short) i)
                    .sex(Sex.M)
                    .neutering(Neutering.N)
                    .foundLocation(String.valueOf(i))
                    .significant(String.valueOf(i))
                    .careName(String.valueOf(i))
                    .careAddr(String.valueOf(i))
                    .careTel(String.valueOf(i))
                    .authority(String.valueOf(i))
                    .authorityPhoneNumber(String.valueOf(i))
                    .build();
            protectingReportRepository.save(protectingReport);

            if (i > 4 && i < 15) {
                InterestProtectingReport interestProtectingReport = InterestProtectingReport.createInterestProtectingReport(user, protectingReport);
                interestProtectingReportRepository.save(interestProtectingReport);
            }

            if (i > 24 && i < 35) {
                InterestProtectingReport interestProtectingReport = InterestProtectingReport.createInterestProtectingReport(user, protectingReport);
                interestProtectingReportRepository.save(interestProtectingReport);
            }
        }

        for(int i=1;i<=67;i++) {
            // 신고 동물 설정
            ReportAnimal reportAnimal = ReportAnimal.builder()
                    .furColor(String.valueOf(i))
                    .breed(breed)
                    .build();
            reportAnimalRepository.save(reportAnimal);
            //=========================================


            //=========================================
            // 신고 동물에 특징 매핑
            ReportedAnimalFeature reportedAnimalFeature = ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature);
            ReportedAnimalFeature reportedAnimalFeature2 = ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature2);
            reportedAnimalFeatureRepository.save(reportedAnimalFeature);
            reportedAnimalFeatureRepository.save(reportedAnimalFeature2);

            //=========================================
            // 신고글 작성
            String tag = "목격신고";
            if (i > 20) {
                tag = "실종신고";
            }

            Report report = Report.createReport(tag, String.valueOf(i), LocalDate.now(), String.valueOf(i), user, reportAnimal);
            reportRepository.save(report);
            //=========================================

            //=========================================
            // 관심 글로 등록
            if (i > 20) {
                InterestReport viewedReport = InterestReport.createInterestReport(user, report);
                interestReportRepository.save(viewedReport);
            }
            //=========================================
        }
    }
}
