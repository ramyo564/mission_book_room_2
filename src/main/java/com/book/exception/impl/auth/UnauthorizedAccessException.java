package com.book.exception.impl.auth;

import com.book.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UnauthorizedAccessException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "예약한 고객과 정보가 일치하지 않습니다.";
    }
}