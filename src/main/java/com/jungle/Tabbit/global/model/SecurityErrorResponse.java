package com.jungle.Tabbit.global.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class SecurityErrorResponse<T> extends CommonResponse<T> {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public String convertToJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }

    public SecurityErrorResponse(boolean success, T data, String message, HttpStatus statusCode) {
        super(success, Optional.ofNullable(data), message, statusCode);
    }

    public static <T> SecurityErrorResponse<T> of(ResponseStatus responseStatus) {
        return new SecurityErrorResponse<>(true, null, responseStatus.getMessage(), responseStatus.getStatusCode());
    }
}
