package com.jungle.Tabbit.global.exception;

import com.jungle.Tabbit.global.model.ResponseStatus;
/**
 * 이미 존재하는 리소스를 생성하려 할 때 사용하는 예외
 */
public class DuplicatedException extends BusinessLogicException {
    public DuplicatedException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public DuplicatedException(String message) {
        super(message);
    }
}
