package com.kuit.findyou.domain.user.controller;

import com.kuit.findyou.domain.home.dto.ReportTag;
import com.kuit.findyou.domain.user.dto.PostInterestAnimalRequest;
import com.kuit.findyou.domain.user.service.UserService;
import com.kuit.findyou.global.common.exception.BadRequestException;
import com.kuit.findyou.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.kuit.findyou.global.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;
    @PostMapping("interest-animals")
    public BaseResponse<Long> postInterestAnimal(@RequestBody PostInterestAnimalRequest request){
        // 토큰 구현이 안된 상태라서 미리 저장된 사용자 활용
        Long userId = 1L;
        log.info("[postInterestAnimal] userId = {} request = {}", userId, request);
        checkTagIsValid(request);
        if(isProtectingReport(request)) {
            Long id = userService.saveInterestProtectingAnimal(userId, request);
            log.info("[postInterestAnimal] id = {}", id);
            return new BaseResponse<>(null);
        }
        Long id = userService.saveInterestReportAnimal(userId, request);
        log.info("[postInterestAnimal] id = {}", id);
        return new BaseResponse<>(null);
    }

    @DeleteMapping("interest-animals/protecting-animals/{interest_protecting_animal_id}")
    public BaseResponse<Object> deleteInterestProtectingAnimal(@PathVariable("interest_protecting_animal_id") Long interestProtectingReportId){
        log.info("[deleteInterestProtectingAnimal] interestProtectingReportId = {}", interestProtectingReportId);
        long userId = 2L;
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
