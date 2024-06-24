package com.jungle.Tabbit.domain.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.jungle.Tabbit.domain.fcm.dto.FcmRequestDto;
import com.jungle.Tabbit.domain.fcm.dto.FcmResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class FcmService {

    private final WebClient webClient;

    public FcmService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://fcm.googleapis.com/v1/projects/tabbit-c1857").build();
    }

    public void sendMessageTo(FcmRequestDto fcmRequestDto) throws IOException {

        String message = makeMessage(fcmRequestDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getAccessToken());

        String API_URL = "https://fcm.googleapis.com/v1/projects/tabbit-c1857/messages:send";

        webClient.post()
                .uri(API_URL)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .bodyValue(message)
                .retrieve()
                .toEntity(String.class)
                .doOnSuccess(res -> System.out.println(res.getStatusCode()))
                .doOnError(err -> System.err.println("Error sending FCM message: " + err.getMessage()))
                .block();
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/tabbit-c1857-firebase-adminsdk-d0wb2-29074368c4.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    private String makeMessage(FcmRequestDto fcmRequestDto) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        FcmResponseDto fcmResponseDto = FcmResponseDto.builder()
                .message(FcmResponseDto.Message.builder()
                        .token(fcmRequestDto.getToken())
                        .notification(FcmResponseDto.Notification.builder()
                                .title(fcmRequestDto.getTitle())
                                .body(fcmRequestDto.getBody())
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return om.writeValueAsString(fcmResponseDto);
    }
}
