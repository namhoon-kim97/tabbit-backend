package com.jungle.Tabbit.domain.order.dto.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@Setter
@Schema(description = "메뉴 생성/수정 요청 DTO")
public class MenuRequestDto {
    @Schema(description = "메뉴명")
    private String name;
    @Schema(description = "가격")
    private Long price;
    @Schema(description = "설명")
    private String description;
    @Schema(description = "카테고리Id")
    private Long categoryId;
    @Schema(description = "이미지 파일")
    private MultipartFile multipartFile;
}
