package com.jungle.Tabbit.domain.order.dto.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "카테고리 생성/수정 요청 DTO")
public class MenuCategoryRequestDto {
    @Schema(description = "카테고리명")
    private String categoryName;
}
