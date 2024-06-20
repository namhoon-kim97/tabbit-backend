package com.jungle.Tabbit.global.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SecurityErrorResponse extends CommonResponse {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public String convertToJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }

    public static SecurityErrorResponse of(ResponseStatus responseStatus) {
        return (SecurityErrorResponse) SecurityErrorResponse.fail(responseStatus);
    }
}
