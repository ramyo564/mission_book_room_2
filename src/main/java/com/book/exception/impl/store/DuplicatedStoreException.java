package com.book.exception.impl.store;

import com.book.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class DuplicatedStoreException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "중복된 상점입니다. 다시 한 번 확인해 주세요";
    }
}
