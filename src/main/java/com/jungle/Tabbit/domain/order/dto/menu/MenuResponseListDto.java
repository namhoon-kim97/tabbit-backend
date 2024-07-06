package com.jungle.Tabbit.domain.order.dto.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "메뉴 리스트 응답 DTO")
public class MenuResponseListDto {
    @Schema(description = "맛집 이름")
    private String name;
    @Schema(description = "카테고리 리스트")
    private List<String> categoryList;
    @Schema(description = "메뉴 리스트")
    private List<MenuResponseDto> menuList;

    public static MenuResponseListDto of(String name, List<String> categoryList, List<MenuResponseDto> menuList) {
        return MenuResponseListDto.builder()
                .name(name)
                .categoryList(categoryList)
                .menuList(menuList)
                .build();
    }
}
