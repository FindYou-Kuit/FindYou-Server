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
import com.kuit.findyou.global.common.response.BaseErrorResponse;
import com.kuit.findyou.global.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Getter;
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

    @Operation(
            summary = "사용자의 관심동물 조회",
            description = "사용자의 관심 동물 리스트를 무한스크롤 형식으로 조회할 수 있는 API입니다. 현재는 토큰이 구현되지 않았지만 데모 데이에서는 엑세스 토큰이 필요합니다." +
                    " 이 API로 조회하려면 lastInterestReportId, lastInterestProtectId를 지정해야합니다. 처음에는 long의 최대값인 9223372036854775807 값을 지정해야 합니다. "
    )
    @Parameter(name = "lastInterestReportId", description = "조회된 관심동물 중 마지막 관심 동물의 id")
    @Parameter(name = "lastInterestProtectId", description = "조회된 관심동물 중 마지막 보호중동물의 id")
    @GetMapping("/interest-animals")
    public BaseResponse<GetInterestAnimalCursorPageDto> getInterestAnimals(@RequestParam(value = "lastInterestReportId") Long lastInterestReportId, @RequestParam(name = "lastInterestProtectId") Long lastInterestProtectId){
        long userId = 1L;
        int size = 20;
        return new BaseResponse<>(interestAnimalRetrieveService.getInterestAnimalCursorPage(userId, lastInterestReportId, lastInterestProtectId, size));
    }

    @Operation(
            summary = "관심동물 추가 - 보호중동물 추가",
            description = "보호중동물을 관심동물로 추가하는 api입니다. "
    )
    @PostMapping("/interest-animals/protecting-animals")
    public BaseResponse<Long> postInterestProtectingAnimal(@RequestBody PostInterestAnimalRequest request){
        // 토큰 구현이 안된 상태라서 미리 저장된 사용자 활용
        Long userId = 1L;
        log.info("[postInterestProtectingAnimal] userId = {} request = {}", userId, request);

        Long id = userService.saveInterestProtectingAnimal(userId, request);
        log.info("[postInterestProtectingAnimal] id = {}", id);
        return new BaseResponse<>(id);
    }

    @Operation(
            summary = "관심동물 추가 - 신고동물 추가",
            description = "신고동물을 관심동물로 추가하는 api입니다. "
    )
    @PostMapping("/interest-animals/report-animals")
    public BaseResponse<Long> postInterestReportAnimal(@RequestBody PostInterestAnimalRequest request){
        // 토큰 구현이 안된 상태라서 미리 저장된 사용자 활용
        Long userId = 1L;
        log.info("[postInterestReportAnimal] userId = {} request = {}", userId, request);

        Long id = userService.saveInterestReportAnimal(userId, request);
        log.info("[postInterestReportAnimal] id = {}", id);
        return new BaseResponse<>(id);
    }

    @Operation(
            summary = "관심동물 삭제 - 보호중동물 삭제 ",
            description = "보호중동물을 관심동물에서 삭제하는 api입니다. "
    )
    @Parameter(name = "interest_protecting_animal_id", description = "삭제할 관심동물의 id")
    @DeleteMapping("interest-animals/protecting-animals/{interest_protecting_animal_id}")
    public BaseResponse<Object> deleteInterestProtectingAnimal(@PathVariable("interest_protecting_animal_id") Long interestProtectingReportId){
        log.info("[deleteInterestProtectingAnimal] interestProtectingReportId = {}", interestProtectingReportId);
        long userId = 1L;
        userService.removeInterestProtectingAnimal(userId, interestProtectingReportId);
        return new BaseResponse<>(null);
    }

    @Operation(
            summary = "관심동물 삭제 - 신고동물 삭제 ",
            description = "신고동물을 관심동물에서 삭제하는 api입니다. "
    )
    @Parameter(name = "report_animal_id", description = "삭제할 관심동물의 id")
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

    @Operation(
            summary = "사용자의 신고내역 조회",
            description = "사용자의 신고 내역을 조회할 수 있는 API입니다. 이 API로 조회하려면  lastReportId 쿼리파라미터를 지정해야합니다. " +
                    "처음에는 long의 최대값인 9223372036854775807을 넘겨주세요. 이후에는 마지막 응답으로부터 받은 값을 넘겨주세요."
    )
    @Parameter(name = "lastReportId", description = "조회할 신고글 중 마지막 신고글의 id")
    @GetMapping("/reports")
    public BaseResponse<GetUsersReportsResponse> getUsersReports(@RequestParam("lastReportId") Long lastReportId){
        Long userId = 1L;
        int size = 20;
        return new BaseResponse<>(userService.findReports(userId, lastReportId, size));
    }
}
