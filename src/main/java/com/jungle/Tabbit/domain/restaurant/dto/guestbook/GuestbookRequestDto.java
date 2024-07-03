package com.jungle.Tabbit.domain.restaurant.dto.guestbook;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "방명록 생성 요청 DTO")
public class GuestbookRequestDto {
    @Schema(description = "방명록 내용")
    private String content;
}
