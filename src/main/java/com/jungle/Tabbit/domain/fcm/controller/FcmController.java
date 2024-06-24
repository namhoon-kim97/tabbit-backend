package com.jungle.Tabbit.domain.fcm.controller;

import com.jungle.Tabbit.domain.fcm.dto.FcmRequestDto;
import com.jungle.Tabbit.domain.fcm.service.FcmService;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/send")
    public CommonResponse<?> pushMessage(@RequestBody @Validated FcmRequestDto fcmRequestDto) throws IOException {
        log.debug("[+] 푸시 메시지를 전송합니다. ");
        fcmService.sendMessageTo(fcmRequestDto);

        return CommonResponse.success(ResponseStatus.SUCCESS_OK);
    }
}