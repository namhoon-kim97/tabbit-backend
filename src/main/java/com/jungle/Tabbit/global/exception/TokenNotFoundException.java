package com.jungle.Tabbit.global.exception;

import com.jungle.Tabbit.global.model.ResponseStatus;

public class TokenNotFoundException extends BusinessLogicException{
    public TokenNotFoundException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public TokenNotFoundException(String message) {
        super(message);
    }
}
