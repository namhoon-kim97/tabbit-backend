package com.jungle.Tabbit.domain.restaurant.dto.guestbook;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "방명록 리스트 응답 DTO")
public class GuestbookResponseListDto {
    @Schema(description = "방명록 리스트")
    private List<GuestbookResponseDto> guestbookResponseList;
    @Schema(description = "방명록 작성 가능 여부")
    private Boolean isWritable;

    public static GuestbookResponseListDto of(List<GuestbookResponseDto> guestbookResponseList, Boolean isWritable) {
        return GuestbookResponseListDto.builder()
                .guestbookResponseList(guestbookResponseList)
                .isWritable(isWritable)
                .build();
    }
}
