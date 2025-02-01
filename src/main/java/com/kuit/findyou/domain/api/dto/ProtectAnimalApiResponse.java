package com.kuit.findyou.domain.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProtectAnimalApiResponse {

    @JsonProperty("response")
    private Response response;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        @JsonProperty("header")
        private ProtectAnimalApiResponseHeader header;

        @JsonProperty("body")
        private ProtectAnimalApiResponseBody body;
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProtectAnimalApiResponseHeader {
        @JsonProperty("reqNo")
        private String requestNumber;

        @JsonProperty("resultCode")
        private String resultCode;

        @JsonProperty("resultMsg")
        private String resultMessage;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProtectAnimalApiResponseBody {
        @JsonProperty("items")
        private Items items;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Items {
        @JsonProperty("item")
        private List<Item> item;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Item {
        @JsonProperty("desertionNo")
        private String desertionNo;  // 유기 번호

        @JsonProperty("filename")
        private String filename;     // 작은 이미지

        @JsonProperty("happenDt")
        private String happenDt;     // 접수일

        @JsonProperty("happenPlace")
        private String happenPlace;  // 발견장소

        @JsonProperty("kindCd")
        private String kindCd;       // 품종 - [개] 믹스견

        @JsonProperty("colorCd")
        private String colorCd;      // 색상 - 흰색&검은색

        @JsonProperty("age")
        private String age;          // 나이 - 2019(년생)

        @JsonProperty("weight")
        private String weight;       // 몸무게 - 0.69(Kg)

        @JsonProperty("noticeNo")
        private String noticeNo;     // 공고 번호

        @JsonProperty("noticeSdt")
        private String noticeSdt;    // 공고 시작일

        @JsonProperty("noticeEdt")
        private String noticeEdt;    // 공고 종료일

        @JsonProperty("popfile")
        private String popfile;      // 큰 이미지

        @JsonProperty("processState")
        private String processState; // 상태

        @JsonProperty("sexCd")
        private String sexCd;        // 성별

        @JsonProperty("neuterYn")
        private String neuterYn;     // 중성화 여부

        @JsonProperty("specialMark")
        private String specialMark;  // 특징

        @JsonProperty("careNm")
        private String careNm;       // 보호소 이름

        @JsonProperty("careTel")
        private String careTel;      // 보호소 전화번호

        @JsonProperty("careAddr")
        private String careAddr;     // 보호 장소

        @JsonProperty("orgNm")
        private String orgNm;        // 관할 기관

        @JsonProperty("chargeNm")
        private String chargeNm;     // 담당자

        @JsonProperty("officetel")
        private String officetel;    // 담당자연락처


        public String extractBreed() {
            // 첫 번째 공백의 위치를 찾음
            int firstSpaceIndex = kindCd.indexOf(" ");

            // 공백이 없거나 마지막 문자가 공백인 경우 빈 문자열 반환
            if (firstSpaceIndex == -1 || firstSpaceIndex == kindCd.length() - 1) {
                return "";
            }

            // 첫 번째 공백 이후의 모든 문자열을 그대로 가져와서 반환
            return kindCd.substring(firstSpaceIndex + 1);
        }

        public String extractSpecies() {
            // 대괄호 안의 내용만 추출하는 정규식
            String regex = "\\[(.*?)\\]";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(kindCd);

            if (matcher.find()) {
                return matcher.group(1);  // 매칭된 그룹을 반환
            } else {
                return "";  // 매칭되지 않으면 빈 문자열 반환
            }
        }


        public String extractYear() {
            // 숫자와 괄호 이전의 연도만 추출
            return age.split("\\(")[0].trim();// 불필요한 공백을 제거
        }


        public String extractWeight() {
            // 괄호 이전의 몸무게만 추출
            String weight = this.weight.split("\\(")[0]; // "("를 기준으로 분리하고, 첫 번째 부분인 몸무게를 반환

            return weight.replace(',', '.').trim(); // 불필요한 공백을 제거
        }


        public String extractFurColor() {
            // &를 , 로 대체
            return colorCd.replace("&", ", ");
        }


        public LocalDate changeToLocalDate(String date) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            return LocalDate.parse(date, formatter);
        }

    }

}
