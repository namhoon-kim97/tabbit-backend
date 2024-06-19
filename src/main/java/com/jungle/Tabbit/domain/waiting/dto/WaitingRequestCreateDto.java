package com.jungle.Tabbit.domain.waiting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WaitingRequestCreateDto {
    private int peopleNumber;
    private int waitingNumber;
}
