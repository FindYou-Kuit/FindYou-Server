package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.ProtectingReportInfoDTO;
import com.kuit.findyou.domain.report.model.ProtectingReport;
import com.kuit.findyou.domain.report.model.ViewedProtectingReport;
import com.kuit.findyou.domain.report.repository.ProtectingReportRepository;
import com.kuit.findyou.domain.report.repository.ViewedProtectingReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProtectingAnimalInfoService {

    private final ProtectingReportRepository protectingReportRepository;
    private final ViewedProtectingReportRepository viewedProtectingReportRepository;
    private final UserRepository userRepository;


    @Transactional
    public ProtectingReportInfoDTO findProtectingReportInfoById(Long protectingReportId, Long userId) {
        ProtectingReport protectingReport = protectingReportRepository.findById(protectingReportId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 보호글입니다."));   // 커스텀 예외로 바꿀수도

        User loginedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));    // 커스텀 예외로 바꿀수도

        // 최근 본 보호글로 등록
        ViewedProtectingReport viewedProtectingReport = ViewedProtectingReport.createViewedProtectingReport(loginedUser, protectingReport);
        viewedProtectingReportRepository.save(viewedProtectingReport);

        boolean interest = loginedUser.isInterestProtectingReport(protectingReportId);

        List<String> tempImages = new ArrayList<>();   // 이미지 관련 로직이 아직 없어서 더미 데이터 삽입
        tempImages.add("image1.jpg");
        tempImages.add("image2.jpg");
        tempImages.add("image3.jpg");

        return ProtectingReportInfoDTO.builder()
                .imageUrls(tempImages)
                .breed(protectingReport.getBreed())
                .tag("보호중")
                .age(String.valueOf(protectingReport.getAge()))
                .weight(String.valueOf(protectingReport.getWeight()))
                .sex(protectingReport.getAnimalSex())
                .happenDate(protectingReport.getHappenDate().toString())
                .furColor(protectingReport.getFurColor())
                .neutering(protectingReport.getAnimalNeutering())
                .significant(protectingReport.getSignificant())
                .noticeNumber(protectingReport.getNoticeNumber())
                .noticeDuration(protectingReport.getNoticeDuration())
                .foundLocation(protectingReport.getFoundLocation())
                .careName(protectingReport.getCareName())
                .careAddr(protectingReport.getCareAddr())
                .careTel(protectingReport.getCareTel())
                .authority(protectingReport.getAuthority())
                .authorityPhoneNumber(protectingReport.getAuthorityPhoneNumber())
                .interest(interest)
                .build();
    }

//    테스트용 데이터
//    @Transactional
//    public void setUp() {
//        User user = User.builder()
//                .name("김상균")
//                .email("ksg001227@naver.com")
//                .password("skcjswo00")
//                .build();
//
//        userRepository.save(user);
//
//
//        for (int i = 1; i <= 10; i++) {
//            ProtectingReport report = ProtectingReport.builder()
//                    .happenDate(LocalDate.now())
//                    .imageUrl(String.valueOf(i))
//                    .species(String.valueOf(i))
//                    .noticeNumber(String.valueOf(i))
//                    .noticeStartDate(LocalDate.now())
//                    .noticeEndDate(LocalDate.now())
//                    .breed(String.valueOf(i))
//                    .furColor(String.valueOf(i))
//                    .weight(3.5F)
//                    .age((short) i)
//                    .sex(Sex.M)
//                    .neutering(Neutering.N)
//                    .foundLocation(String.valueOf(i))
//                    .significant(String.valueOf(i))
//                    .careName(String.valueOf(i))
//                    .careAddr(String.valueOf(i))
//                    .careTel(String.valueOf(i))
//                    .authority(String.valueOf(i))
//                    .authorityPhoneNumber(String.valueOf(i))
//                    .build();
//            protectingReportRepository.save(report);
//
//            if(i > 4) {
//                InterestProtectingReport interestProtectingReport = InterestProtectingReport.createInterestProtectingReport(user, report);
//                interestProtectingReportRepository.save(interestProtectingReport);
//            }
//        }
//    }
}
