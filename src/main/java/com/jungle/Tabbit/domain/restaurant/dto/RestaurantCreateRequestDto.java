package com.jungle.Tabbit.domain.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class RestaurantCreateRequestDto {
    private String placeName;  // 업체명
    private String categoryCd;  // 카테고리 코드
    private String addressName; // 지번 주소
    private String roadAddressName; // 도로명 주소
    private String detailAddress; // 상세 주소
    private String sido; // 시도
    private String sigungu; // 시군구
    private String eupmyeondong; // 읍면동
    private BigDecimal longitude; // 경도
    private BigDecimal latitude; // 위도
    private Long estimatedTimePerTeam; // 팀당 예상 대기 시간
    private String openingHours; // 운영시간
    private String breakTime; // 브레이크 타임
    private String holidays; // 휴무일
    private String restaurantNumber; // 전화번호
    private String description; // 매장소개
}
