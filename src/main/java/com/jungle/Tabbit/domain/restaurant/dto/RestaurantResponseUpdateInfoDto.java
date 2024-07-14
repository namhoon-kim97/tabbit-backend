package com.jungle.Tabbit.domain.restaurant.dto;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.entity.RestaurantDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@Schema(description = "맛집 수정 정보 응답 DTO")
public class RestaurantResponseUpdateInfoDto {

    @Schema(description = "업체명")
    private String placeName;  // 업체명
    @Schema(description = "이미지 경로")
    private String imageUrl; // 이미지 경로
    @Schema(description = "카테고리 코드")
    private String categoryCd;  // 카테고리 코드
    @Schema(description = "카테고리 이름")
    private String categoryName; // 카테고리 이름
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

    public static RestaurantResponseUpdateInfoDto of(Restaurant restaurant, RestaurantDetail restaurantDetail) {
        return RestaurantResponseUpdateInfoDto.builder()
                .placeName(restaurant.getName())
                .imageUrl(restaurant.getImageUrl())
                .categoryCd(restaurant.getCategory().getCategoryCd())
                .categoryName(restaurant.getCategory().getCategoryName())
                .addressName(restaurant.getAddress().getStreetAddress())
                .roadAddressName(restaurant.getAddress().getRoadAddress())
                .detailAddress(restaurant.getAddress().getDetailAddress())
                .sido(restaurant.getAddress().getSido())
                .sigungu(restaurant.getAddress().getSigungu())
                .eupmyeondong(restaurant.getAddress().getEupmyeondong())
                .longitude(restaurant.getLongitude())
                .latitude(restaurant.getLatitude())
                .estimatedTimePerTeam(restaurant.getEstimatedTimePerTeam())
                .openingHours(restaurantDetail.getOpeningHours())
                .breakTime(restaurantDetail.getBreakTime())
                .holidays(restaurantDetail.getHolidays())
                .restaurantNumber(restaurantDetail.getRestaurantNumber())
                .description(restaurantDetail.getDescription())
                .build();
    }
}
