package com.book.exception.impl.store;

import com.book.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyDeletedStoreException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 삭제된 상점입니다.";
    }
}
