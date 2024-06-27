package com.jungle.Tabbit.domain.waiting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "웨이팅 업데이트 요청 DTO")
public class WaitingRequestUpdateDto {

    @Schema(description = "웨이팅 번호", example = "5")
    private int waitingNumber;
}
