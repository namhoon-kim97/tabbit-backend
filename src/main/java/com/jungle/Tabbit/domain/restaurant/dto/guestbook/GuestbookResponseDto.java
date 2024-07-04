package com.jungle.Tabbit.domain.restaurant.dto.guestbook;

import com.jungle.Tabbit.domain.restaurant.entity.Guestbook;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "방명록 응답 DTO")
public class GuestbookResponseDto {

    @Schema(description = "방명록 내용")
    private String content;

    @Schema(description = "방명록 이미지 경로")
    private String imageUrl;

    public static GuestbookResponseDto of(Guestbook guestbook) {
        return GuestbookResponseDto.builder()
                .content(guestbook.getContent())
                .imageUrl(guestbook.getImageUrl())
                .build();
    }
}
