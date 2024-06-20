package com.jungle.Tabbit.global.exception;

import com.jungle.Tabbit.global.model.ResponseStatus;

public class MemberNotFoundException extends BusinessLogicException{

    public MemberNotFoundException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}
