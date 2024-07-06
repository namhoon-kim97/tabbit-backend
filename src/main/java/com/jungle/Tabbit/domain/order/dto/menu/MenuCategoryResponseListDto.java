package com.jungle.Tabbit.domain.order.dto.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "카테고리 리스트 응답 DTO")
public class MenuCategoryResponseListDto {
    @Schema(description = "카테고리 리스트")
    private List<MenuCategoryResponseDto> menuCategoryList;

    public static MenuCategoryResponseListDto of(List<MenuCategoryResponseDto> menuCategoryList) {
        return MenuCategoryResponseListDto.builder()
                .menuCategoryList(menuCategoryList)
                .build();
    }
}
