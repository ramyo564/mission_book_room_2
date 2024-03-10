package com.book.exception.impl.auth;

import com.book.exception.AbstractException;
import org.springframework.http.HttpStatus;


public class NotAuthException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.FORBIDDEN.value();
    }

    @Override
    public String getMessage() {
        return "권한이 없습니다.";
    }
}
