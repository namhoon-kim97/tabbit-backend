package com.jungle.example_code.global.exception;


import com.Jungle.jungleboard.global.model.ResponseStatus;

/**
 * 인증되지 않은 사용자가 보호된 리소스에 접근하려 할 때 사용하는 예외
 */
public class UnauthorizedException extends BusinessLogicException {

    public UnauthorizedException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
