package com.jungle.Tabbit.domain.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class RestaurantCreateRequestDto {
    private String place_name;  // 업체명
    private String categoryCd;  // 카테고리 코드
    private String address_name; // 지번 주소
    private String road_address_name; // 도로명 주소
    private String detail_address; // 상세 주소
    private String sido; // 시도
    private String sigungu; // 시군구
    private String eupmyeondong; // 읍면동
    private BigDecimal longitude; // 경도
    private BigDecimal latitude; // 위도
    private Long estimatedTimePerTeam; // 팀당 예상 대기 시간
    private String opening_hours; // 운영시간
    private String break_time; // 브레이크 타임
    private String holidays; // 휴무일
    private String restaurant_number; // 전화번호
    private String description; // 매장소개
}
