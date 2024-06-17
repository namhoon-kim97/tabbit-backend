package com.jungle.example_code.global.exception;

import com.jungle.example_code.global.model.ResponseStatus;

import lombok.Getter;

@Getter
public class BusinessLogicException extends RuntimeException {

    private ResponseStatus responseStatus;

    public BusinessLogicException(ResponseStatus responseStatus) {
        super(responseStatus.getMessage());
        this.responseStatus = responseStatus;
    }

    public BusinessLogicException(String message) {
        super(message);
    }
}
