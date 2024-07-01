package com.jungle.Tabbit.global.exception;

import com.jungle.Tabbit.global.model.ResponseStatus;

/**
 * 이미 존재하는 리소스를 생성하려 할 때 사용하는 예외
 */
public class FileUploadException extends BusinessLogicException {
    public FileUploadException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public FileUploadException(String message) {
        super(message);
    }
}
