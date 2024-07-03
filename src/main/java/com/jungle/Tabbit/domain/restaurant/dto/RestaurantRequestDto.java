package com.jungle.Tabbit.domain.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Setter
@Schema(description = "맛집 생성/수정 요청 DTO")
public class RestaurantRequestDto {
    @Schema(description = "업체명")
    private String placeName;  // 업체명
    @Schema(description = "카테고리 코드")
    private String categoryCd;  // 카테고리 코드
    @Schema(description = "지번 주소")
    private String addressName; // 지번 주소
    @Schema(description = "도로명 주소")
    private String roadAddressName; // 도로명 주소
    @Schema(description = "상세 주소")
    private String detailAddress; // 상세 주소
    @Schema(description = "시도")
    private String sido; // 시도
    @Schema(description = "시군구")
    private String sigungu; // 시군구
    @Schema(description = "읍면동")
    private String eupmyeondong; // 읍면동
    @Schema(description = "경도")
    private BigDecimal longitude; // 경도
    @Schema(description = "위도")
    private BigDecimal latitude; // 위도
    @Schema(description = "팀당 예상 대기 시간")
    private Long estimatedTimePerTeam; // 팀당 예상 대기 시간
    @Schema(description = "운영시간")
    private String openingHours; // 운영시간
    @Schema(description = "브레이크 타임")
    private String breakTime; // 브레이크 타임
    @Schema(description = "휴무일")
    private String holidays; // 휴무일
    @Schema(description = "전화번호")
    private String restaurantNumber; // 전화번호
    @Schema(description = "매장소개")
    private String description; // 매장소개

    private MultipartFile multipartFile;
}
