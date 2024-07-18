package com.jungle.Tabbit.domain.order.dto.menu;

import com.jungle.Tabbit.domain.order.entity.Menu;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "메뉴 응답 DTO")
public class MenuResponseDto {

    @Schema(description = "메뉴ID")
    private Long menuId;
    @Schema(description = "메뉴명")
    private String name;
    @Schema(description = "가격")
    private Long price;
    @Schema(description = "설명")
    private String description;
    @Schema(description = "이미지 경로")
    private String imageUrl;
    @Schema(description = "카테고리ID")
    private Long categoryId;
    @Schema(description = "카테고리명")
    private String categoryName;

    public static MenuResponseDto of(Menu menu) {
        return MenuResponseDto.builder()
                .menuId(menu.getMenuId())
                .name(menu.getName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .imageUrl(menu.getImageUrl())
                .categoryId(menu.getCategory().getCategoryId())
                .categoryName(menu.getCategory().getCategoryName())
                .build();
    }
}
