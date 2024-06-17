package com.jungle.example_code.global.exception;


import com.jungle.example_code.global.model.ResponseStatus;
public class WrongPasswordException extends BusinessLogicException {

    public WrongPasswordException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public WrongPasswordException(String message) {
        super(message);
    }
}