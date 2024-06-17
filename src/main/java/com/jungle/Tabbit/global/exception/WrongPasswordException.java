package com.jungle.Tabbit.global.exception;


import com.jungle.Tabbit.global.model.ResponseStatus;
public class WrongPasswordException extends BusinessLogicException {

    public WrongPasswordException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public WrongPasswordException(String message) {
        super(message);
    }
}