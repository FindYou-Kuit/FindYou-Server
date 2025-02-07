package com.kuit.findyou.domain.report.controller;

import com.kuit.findyou.domain.report.dto.*;
import com.kuit.findyou.domain.report.service.*;
import com.kuit.findyou.global.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportAnimalInfoService reportAnimalInfoService;
    private final ProtectingAnimalInfoService protectingAnimalInfoService;
    private final ProtectingAnimalRetrieveService protectingAnimalRetrieveService;
    private final ReportAnimalRetrieveService reportAnimalRetrieveService;
    private final AnimalRetrieveService animalRetrieveService;
    private final MissingReportPostService missingReportPostService;
    private final WitnessReportPostService witnessReportPostService;
    private final GetAllBreedsService getAllBreedsService;
    private final BreedValidateService breedValidateService;


    // test에 필요한 레포지토리들
//    private final UserRepository userRepository;
//    private final ProtectingReportRepository protectingReportRepository;
//    private final InterestProtectingReportRepository interestProtectingReportRepository;
//    private final BreedRepository breedRepository;
//    private final AnimalFeatureRepository animalFeatureRepository;
//    private final ReportRepository reportRepository;
//    private final InterestReportRepository interestReportRepository;


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
    public BaseResponse<ProtectingReportCardDTO> retrieveProtectingReports(
            @RequestParam("lastProtectId") Long lastProtectId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "species", required = false) String species,
            @RequestParam(value = "breeds", required = false) List<String> breeds,
            @RequestParam(value = "location", required = false) String location) {
        ProtectingReportCardDTO protectingReportCardDTO =
                protectingAnimalRetrieveService.retrieveProtectingReportCardsWithFilters(1L, lastProtectId, startDate, endDate, species, breeds, location);

        return new BaseResponse<>(protectingReportCardDTO);
    }

    @GetMapping("/report-animals")
    public BaseResponse<ReportCardDTO> retrieveReports(
            @RequestParam("lastReportId") Long lastReportId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "species", required = false) String species,
            @RequestParam(value = "breeds", required = false) List<String> breeds,
            @RequestParam(value = "location", required = false) String location) {
        ReportCardDTO reportCardDTO = reportAnimalRetrieveService.retrieveReportCardsWithFilters(1L, lastReportId, startDate, endDate, species, breeds, location);

        return new BaseResponse<>(reportCardDTO);
    }

    @GetMapping
    public BaseResponse<TotalCardDTO> retrieveAll(
            @RequestParam("lastProtectId") Long lastProtectId,
            @RequestParam("lastReportId") Long lastReportId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "species", required = false) String species,
            @RequestParam(value = "breeds", required = false) List<String> breeds,
            @RequestParam(value = "location", required = false) String location) {
        TotalCardDTO totalCardDTO = animalRetrieveService.retrieveTotalCardsWithFilters(1L, lastProtectId, lastReportId, startDate, endDate, species, breeds, location);

        return new BaseResponse<>(totalCardDTO);
    }

    @PostMapping("/new-missing-reports")
    public BaseResponse<Void> postMissingReport(@RequestBody MissingReportDTO requestDTO) {
        missingReportPostService.createReport(requestDTO);
        return new BaseResponse<>(null);
    }
    @PostMapping("/new-witness-reports")
    public BaseResponse<Void> postWitnessReport(@RequestBody WitnessReportDTO requestDTO) {
        witnessReportPostService.createReport(requestDTO);
        return new BaseResponse<>(null);
    }

    @Operation(summary = "품종 전체 반환", description = "모든 품종 정보를 반환합니다.")
    @GetMapping("/breeds")
    public BaseResponse<List<BreedResponseDTO>> getAllBreeds() {
        return new BaseResponse<>(getAllBreedsService.getAllBreeds());
    }

    @Operation(summary = "품종 검증", description = "입력으로 전달된 품종이 DB에 존재하는지 검증합니다.")
    @GetMapping("/breeds/validation")
    public BaseResponse<BreedValidateResponseDTO> validateBreed(
            @Parameter(required = true, description = "DB에 존재하는 지 검증할 품종")
            @RequestParam String breedName) {
        return new BaseResponse<>(breedValidateService.validateBreed(breedName));
    }

//    @PostConstruct
//    public void init() {
//        User user = User.builder()
//                .name("김상균")
//                .email("ksg001227@naver.com")
//                .password("skcjswo00")
//                .build();
//
//        userRepository.save(user);
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
//        // 동물 특징 생성
//        AnimalFeature animalFeature = AnimalFeature.builder().featureValue("순해요").build();
//        AnimalFeature animalFeature2 = AnimalFeature.builder().featureValue("물어요").build();
//        animalFeatureRepository.save(animalFeature);
//        animalFeatureRepository.save(animalFeature2);
//        //=========================================
//
//
//        for (int i = 1; i <= 41; i++) {
//            ProtectingReport protectingReport = ProtectingReport.builder()
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
//            protectingReportRepository.save(protectingReport);
//
//            if (i > 4 && i < 15) {
//                InterestProtectingReport interestProtectingReport = InterestProtectingReport.createInterestProtectingReport(user, protectingReport);
//                interestProtectingReportRepository.save(interestProtectingReport);
//            }
//
//            if (i > 24 && i < 35) {
//                InterestProtectingReport interestProtectingReport = InterestProtectingReport.createInterestProtectingReport(user, protectingReport);
//                interestProtectingReportRepository.save(interestProtectingReport);
//            }
//        }
//
//        for(int i=1;i<=67;i++) {
//            // 신고 동물 설정
//            ReportAnimal reportAnimal = ReportAnimal.builder()
//                    .furColor(String.valueOf(i))
//                    .breed(breed)
//                    .build();
//            //=========================================
//
//
//            //=========================================
//            // 신고 동물에 특징 매핑
//            ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature);
//            ReportedAnimalFeature.createReportedAnimalFeature(reportAnimal, animalFeature2);
//
//            //=========================================
//            //이미지 객체 생성
//            Image image1 = Image.createImage("C:/images/cloud/1.jpg", UUID.randomUUID().toString());
//            Image image2 = Image.createImage("C:/images/cloud/2.jpg", UUID.randomUUID().toString());
//
//            List<Image> images = new ArrayList<>();
//            images.add(image1);
//            images.add(image2);
//            //=========================================
//
//            //=========================================
//            // 신고글 작성
//            String tag = "목격신고";
//            if (i > 20) {
//                tag = "실종신고";
//            }
//
//            Report report = Report.createReport(tag, String.valueOf(i), LocalDate.now(), String.valueOf(i), user, reportAnimal, images);
//            reportRepository.save(report);
//            //=========================================
//
//            //=========================================
//            // 관심 글로 등록
//            if (i > 20) {
//                InterestReport viewedReport = InterestReport.createInterestReport(user, report);
//                interestReportRepository.save(viewedReport);
//            }
//            //=========================================
//
//
//        }
//    }
}
