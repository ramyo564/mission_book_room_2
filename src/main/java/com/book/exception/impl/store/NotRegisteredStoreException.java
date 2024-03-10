package com.book.exception.impl.store;

import com.book.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotRegisteredStoreException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "등록되지 않은 상점입니다. 입력 정보를 다시 한 번 확인해 주세요";
    }
}
