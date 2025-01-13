package com.kuit.findyou.domain.report.service;

import com.kuit.findyou.domain.auth.model.User;
import com.kuit.findyou.domain.auth.repository.UserRepository;
import com.kuit.findyou.domain.report.dto.ReportInfoDTO;
import com.kuit.findyou.domain.report.model.*;
import com.kuit.findyou.domain.report.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportAnimalInfoService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ViewedReportRepository viewedReportRepository;

    public ReportInfoDTO findReportInfoById(Long reportId, Long userId) {

        // 인자로 넘어온 id 에 맞는 신고글 정보
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신고글입니다."));  // 커스텀 예외로 바꿀수도


        // 현재 로그인중인 유저 정보
        User loginedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));    // 커스텀 예외로 바꿀수도


        // 신고 동물 정보
        ReportAnimal reportAnimal = report.getReportAnimal();

        // 동물 특징 정보들
        List<String> animalFeatureList = new ArrayList<>();
        for(ReportedAnimalFeature reportedAnimalFeature : reportAnimal.getReportedAnimalFeatures()) {
            animalFeatureList.add(reportedAnimalFeature.getFeature().getFeatureValue());
        }

        // 신고자 정보
        User reportUser = report.getUser();

        // 최근 본 신고글로 등록
        ViewedReport viewedReport = ViewedReport.createViewedReport(loginedUser, report);
        viewedReportRepository.save(viewedReport);

        boolean interest = loginedUser.isInterestReport(reportId);

        List<String> tempImages = new ArrayList<>();  // 이미지 관련 로직이 아직 없어서 더미 데이터 삽입
        tempImages.add("image1.jpg");
        tempImages.add("image2.jpg");
        tempImages.add("image3.jpg");

        return ReportInfoDTO.builder()
                .imageUrls(tempImages)
                .tag(report.getTag())
                .breed(reportAnimal.getBreed().getSpeciesAndBreed())
                .furColor(reportAnimal.getFurColor())
                .userName(reportUser.getName())
                .writeDate(report.getCreatedAt().toLocalDate().toString())
                .eventDate(report.getEventDate().toString())
                .foundLocation(report.getFoundLocation())
                .features(animalFeatureList)
                .additionalDescription(report.getAdditionalDescription())
                .interest(interest)
                .build();
    }


//    테스트용 데이터
//    @Transactional
//    public void setUp() {
//        //=========================================
//        // 유저 설정
//        User user = User.builder()
//                .name("김상균")
//                .email("ksg001227@naver.com")
//                .password("skcjswo00")
//                .build();
//
//        userRepository.save(user);
//        //=========================================
//
//        //=========================================
//        // 품종, 축종 설정
//        Breed breed = Breed.builder()
//                .name("시츄")
//                .species("개")
//                .build();
//        breedRepository.save(breed);
//        //=========================================
//
//        //=========================================
//        // 신고 동물 설정
//        ReportAnimal reportAnimal = ReportAnimal.builder()
//                .furColor("흰색, 검은색")
//                .tag("목격신고")
//                .breed(breed)
//                .build();
//        reportAnimalRepository.save(reportAnimal);
//        //=========================================
//
//        //=========================================
//        // 동물 특징 생성
//        AnimalFeature animalFeature = AnimalFeature.builder().featureValue("순해요").build();
//        AnimalFeature animalFeature2 = AnimalFeature.builder().featureValue("물어요").build();
//        animalFeatureRepository.save(animalFeature);
//        animalFeatureRepository.save(animalFeature2);
//        //=========================================
//
//        //=========================================
//        // 신고 동물에 특징 매핑
//        ReportedAnimalFeature reportedAnimalFeature = ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature);
//        ReportedAnimalFeature reportedAnimalFeature2 = ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature2);
//        reportedAnimalFeatureRepository.save(reportedAnimalFeature);
//        reportedAnimalFeatureRepository.save(reportedAnimalFeature2);
//
//        //=========================================
//        // 신고글 작성
//        Report report = Report.createReport("목격 신고", "내집앞", LocalDate.now(), "예쁘게 생김", user, reportAnimal);
//        reportRepository.save(report);
//        //=========================================
//
//        //=========================================
//        // 관심 글로 등록
//        InterestReport viewedReport = InterestReport.createInterestReport(user, report);
//        interestReportRepository.save(viewedReport);
//        //=========================================
//    }
}
