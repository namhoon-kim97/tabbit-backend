package com.jungle.Tabbit.global.exception;

import com.jungle.Tabbit.global.model.ResponseStatus;

public class LoginFailException extends BusinessLogicException {

    public LoginFailException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public LoginFailException(String message) {
        super(message);
    }
}
