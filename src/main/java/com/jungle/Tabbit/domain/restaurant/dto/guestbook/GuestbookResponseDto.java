package com.jungle.Tabbit.domain.restaurant.dto.guestbook;

import com.jungle.Tabbit.domain.restaurant.entity.Guestbook;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "방명록 응답 DTO")
public class GuestbookResponseDto {
    @Schema(description = "매핑 ID")
    private Long mappingId;
    @Schema(description = "방명록 내용")
    private String content;

    public static GuestbookResponseDto of(Guestbook guestbook) {
        return GuestbookResponseDto.builder()
                .mappingId(guestbook.getMappingId())
                .content(guestbook.getContent())
                .build();
    }

    public static GuestbookResponseDto emptyOf(Long mappingId) {
        return GuestbookResponseDto.builder()
                .mappingId(mappingId)
                .content("")
                .build();
    }
}
