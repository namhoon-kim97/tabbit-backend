package com.jungle.Tabbit.global.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResponse<T> {

    private boolean success;

    private Optional<T> data;

    private String message;

    private HttpStatus statusCode;

    public static <T> CommonResponse<T> success(ResponseStatus responseStatus) {
        return CommonResponse.<T>builder()
                .success(true)
                .data(Optional.empty())
                .message(responseStatus.getMessage())
                .statusCode(responseStatus.getStatusCode())
                .build();
    }


    public static <T> CommonResponse<T> success(ResponseStatus responseStatus, T data) {
        return CommonResponse.<T>builder()
                .success(true)
                .data(Optional.ofNullable(data))
                .message(responseStatus.getMessage())
                .statusCode(responseStatus.getStatusCode())
                .build();
    }

    
    public static <T> CommonResponse<T> fail(ResponseStatus responseStatus) {
        return CommonResponse.<T>builder()
                .success(false)
                .data(Optional.empty())
                .message(responseStatus.getMessage())
                .statusCode(responseStatus.getStatusCode())
                .build();
    }

    public static <T> CommonResponse<T> fail(HttpStatus httpStatus, String message) {
        return CommonResponse.<T>builder()
                .success(false)
                .data(Optional.empty())
                .message(message)
                .statusCode(httpStatus)
                .build();
    }

}
