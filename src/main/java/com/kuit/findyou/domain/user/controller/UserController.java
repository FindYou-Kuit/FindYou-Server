package com.kuit.findyou.domain.user.controller;

import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.user.dto.GetUsersReportsResponse;
import com.kuit.findyou.domain.user.dto.NewNicknameRequest;
import com.kuit.findyou.domain.user.dto.ViewedCardDTO;
import com.kuit.findyou.domain.user.dto.GetInterestAnimalCursorPageDto;
import com.kuit.findyou.domain.user.dto.PostInterestAnimalRequest;
import com.kuit.findyou.domain.user.dto.RetrieveViewedAnimalRequest;
import com.kuit.findyou.domain.user.service.InterestAnimalRetrieveService;
import com.kuit.findyou.domain.user.service.UserService;
import com.kuit.findyou.domain.user.service.ViewedAnimalRetrieveService;
import com.kuit.findyou.global.common.exception.BadRequestException;
import com.kuit.findyou.global.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;
    private final ViewedAnimalRetrieveService viewedAnimalRetrieveService;
    private final InterestAnimalRetrieveService interestAnimalRetrieveService;
    @GetMapping("/interest-animals")
    public BaseResponse<GetInterestAnimalCursorPageDto> getInterestAnimals(@RequestParam(value = "lastInterestReportId") Long lastInterestReportId, @RequestParam(name = "lastInterestProtectId") Long lastInterestProtectId){
        long userId = 1L;
        int size = 20;
        return new BaseResponse<>(interestAnimalRetrieveService.getInterestAnimalCursorPage(userId, lastInterestReportId, lastInterestProtectId, size));
    }

    @PostMapping("interest-animals")
    public BaseResponse<Long> postInterestAnimal(@RequestBody PostInterestAnimalRequest request){
        // 토큰 구현이 안된 상태라서 미리 저장된 사용자 활용
        Long userId = 1L;
        log.info("[postInterestAnimal] userId = {} request = {}", userId, request);
        checkTagIsValid(request);
        if(isProtectingReport(request)) {
            Long id = userService.saveInterestProtectingAnimal(userId, request);
            log.info("[postInterestAnimal] id = {}", id);
            return new BaseResponse<>(id);
        }
        Long id = userService.saveInterestReportAnimal(userId, request);
        log.info("[postInterestAnimal] id = {}", id);
        return new BaseResponse<>(id);
    }

    @DeleteMapping("interest-animals/protecting-animals/{interest_protecting_animal_id}")
    public BaseResponse<Object> deleteInterestProtectingAnimal(@PathVariable("interest_protecting_animal_id") Long interestProtectingReportId){
        log.info("[deleteInterestProtectingAnimal] interestProtectingReportId = {}", interestProtectingReportId);
        long userId = 1L;
        userService.removeInterestProtectingAnimal(userId, interestProtectingReportId);
        return new BaseResponse<>(null);
    }

    @DeleteMapping("interest-animals/report-animals/{report_animal_id}")
    public BaseResponse<Object> deleteInterestReportAnimal(@PathVariable("report_animal_id") Long reportId){
        log.info("[deleteInterestReportAnimal] id = {}", reportId);
        Long userId = 1L;
        userService.removeInterestReportAnimal(userId, reportId);
        return new BaseResponse<>(null);
    }

    @Operation(summary = "닉네임 수정", description = "유저의 닉네임을 수정합니다.")
    @PatchMapping("/nickname")
    public BaseResponse<Long> updateNickname(
            @Validated @RequestBody NewNicknameRequest newNickname) {
        // 토큰 구현이 안된 상태라서 미리 저장된 사용자 활용
        Long userId = 1L;
        userService.updateNickname(userId, newNickname.getNewNickname());

        return new BaseResponse<>(null);
    }


    @Operation(summary = "회원 탈퇴", description = "서비스에서 탈퇴합니다. 토큰 값이 필요하나 아직 관련 로직이 부재한 상태입니다")
    @DeleteMapping
    public BaseResponse<Void> deleteUser() {
        // 토큰 구현이 안된 상태라서 미리 저장된 사용자 활용
        Long userId = 1L;
        userService.deleteUser(userId);

        return new BaseResponse<>(null);
    }

    @Operation(summary = "최근 본 동물 조회", description = "최근에 상세 정보를 조회한 신고 동물(신고글), 구조 동물(보호글)을 조회합니다.")
    @GetMapping("/viewed-animals")
    public BaseResponse<ViewedCardDTO> retrieveAllViewed(@Validated @ModelAttribute RetrieveViewedAnimalRequest request) {
        // 토큰 구현이 안된 상태라서 미리 저장된 사용자 활용
        Long userId = 1L;
        ViewedCardDTO viewedCardDTO = viewedAnimalRetrieveService.retrieveAllViewedReports(userId, request.getLastViewedProtectId(), request.getLastViewedReportId());

        return new BaseResponse<>(viewedCardDTO);
    }

    @GetMapping("/reports")
    public BaseResponse<GetUsersReportsResponse> getUsersReports(@RequestParam("lastReportId") Long lastReportId){
        Long userId = 1L;
        int size = 20;
        return new BaseResponse<>(userService.findReports(userId, lastReportId, size));
    }

    private boolean isProtectingReport(PostInterestAnimalRequest request) {
        return request.getTag().equals(ReportTag.PROTECTING.getValue());
    }


    private void checkTagIsValid(PostInterestAnimalRequest request) {
        for(ReportTag tag : ReportTag.values()){
            if(request.getTag().equals(tag.getValue())){
                return;
            }
        }
        throw new BadRequestException(BAD_REQUEST);
    }
}
