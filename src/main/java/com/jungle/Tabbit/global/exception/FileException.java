package com.jungle.Tabbit.global.exception;

import com.jungle.Tabbit.global.model.ResponseStatus;

/**
 * 파일 업로드/로드 할 때 사용하는 예외
 */
public class FileException extends BusinessLogicException {
    public FileException(ResponseStatus responseStatus) {
        super(responseStatus);
    }

    public FileException(String message) {
        super(message);
    }
}
