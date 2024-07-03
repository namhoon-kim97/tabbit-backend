package com.jungle.Tabbit.global.exception;

import com.jungle.Tabbit.global.model.ResponseStatus;

/**
 * 패스워드 변경시 패스워드 불일치 하는경우 발생하는 exception
 */
public class PasswordNotMatchException extends BusinessLogicException{
    public PasswordNotMatchException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public PasswordNotMatchException(String message) {
        super(message);
    }
}
