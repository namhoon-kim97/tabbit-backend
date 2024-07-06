package com.jungle.Tabbit.domain.order.dto.menu;

import com.jungle.Tabbit.domain.order.entity.MenuCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "카테고리 응답 DTO")
public class MenuCategoryResponseDto {

    @Schema(description = "카테고리 id")
    private Long categoryId;

    @Schema(description = "카테고리 이름")
    private String categoryName;

    public static MenuCategoryResponseDto of(MenuCategory menuCategory) {
        return MenuCategoryResponseDto.builder()
                .categoryId(menuCategory.getCategoryId())
                .categoryName(menuCategory.getCategoryName())
                .build();
    }
}
