package com.jungle.Tabbit.global.handler;

import com.jungle.Tabbit.global.model.ResponseErrorFormat;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

//검증 예외(ValidationException)에 대한 처리를 담당하는 클래스

@RequiredArgsConstructor
@Slf4j
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseErrorFormat> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("-------HandleMethodArgumentNotValidException-------", e);

        final ResponseStatus responseStatus = ResponseStatus.FAIL_INVALID_PARAMETER;

        return handleExceptionInternal(e, responseStatus);
    }

    private ResponseEntity<ResponseErrorFormat> handleExceptionInternal(final BindException e,
                                                                        final ResponseStatus responseStatus) {

        return ResponseEntity
                .status(responseStatus.getStatusCode())
                .body(makeResponseErrorFormat(e, responseStatus));
    }

    private ResponseErrorFormat makeResponseErrorFormat(final BindException e,
                                                        final ResponseStatus responseStatus) {

        final List<ResponseErrorFormat.ValidationException> validationExceptions = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ResponseErrorFormat.ValidationException::of)
                .collect(Collectors.toList());

        return ResponseErrorFormat.builder()
                .message(responseStatus.getMessage())
                .statusCode(responseStatus.getStatusCode())
                .validationExceptions(validationExceptions)
                .build();
    }
}
