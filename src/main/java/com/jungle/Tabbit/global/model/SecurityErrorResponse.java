package com.jungle.Tabbit.global.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class SecurityErrorResponse extends CommonResponse {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public String convertToJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }

    public SecurityErrorResponse(boolean success, Optional data, String message, HttpStatus statusCode) {
        super(success, data, message, statusCode);
    }

    public static SecurityErrorResponse of(ResponseStatus responseStatus) {
        return new SecurityErrorResponse(true, null, responseStatus.getMessage(), responseStatus.getStatusCode());
    }
}
