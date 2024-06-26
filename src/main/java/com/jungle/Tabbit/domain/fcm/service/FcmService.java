package com.jungle.Tabbit.domain.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.jungle.Tabbit.domain.fcm.dto.FcmRequestDto;
import com.jungle.Tabbit.domain.fcm.dto.FcmResponseDto;
import com.jungle.Tabbit.global.exception.NotFoundException;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class FcmService {

    private final WebClient webClient;

    public FcmService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://fcm.googleapis.com/v1/projects/tabbit-c1857").build();
    }

    public void sendMessageTo(FcmRequestDto fcmRequestDto) {

        String message = makeMessage(fcmRequestDto);
        System.out.printf("------------message : %s", message); // 메시지 내용 로그 출력
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getAccessToken());

        String API_URL = "https://fcm.googleapis.com/v1/projects/tabbit-c1857/messages:send";
        try {
            webClient.post()
                    .uri(API_URL)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .bodyValue(message)
                    .retrieve()
                    .toEntity(String.class)
                    .doOnSuccess(res -> System.out.println(res.getStatusCode()))
                    .doOnError(err -> System.err.println("Error sending FCM message: " + err.getMessage()))
                    .block();
        } catch (Exception e) {
            log.error("FCM 메시지 전송 실패: {}", fcmRequestDto, e);
        }
    }

    private String getAccessToken() {
        try {
            String firebaseConfigPath = "firebase/tabbit-c1857-firebase-adminsdk-d0wb2-29074368c4.json";

            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }

    private String makeMessage(FcmRequestDto fcmRequestDto) {
        try {
            ObjectMapper om = new ObjectMapper();
            FcmResponseDto fcmResponseDto = FcmResponseDto.builder()
                    .message(FcmResponseDto.Message.builder()
                            .token(fcmRequestDto.getToken())
                            .notification(FcmResponseDto.Notification.builder()
                                    .title(fcmRequestDto.getTitle())
                                    .body(fcmRequestDto.getBody())
                                    .image(null)
                                    .build()
                            )
                            .data(fcmRequestDto.getData())  // 추가된 data 필드 설정
                            .build())
                    .validateOnly(false)
                    .build();

            return om.writeValueAsString(fcmResponseDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new NotFoundException(HttpResponseStatus.INTERNAL_SERVER_ERROR.toString());

        }

    }
}
