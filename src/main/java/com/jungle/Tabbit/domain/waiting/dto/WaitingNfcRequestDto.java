package com.jungle.Tabbit.domain.waiting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "nfc 태그 요청 DTO")
public class WaitingNfcRequestDto {
    @Schema(description = "NFC ID", example = "nfc123456")
    private String nfcId;
}
