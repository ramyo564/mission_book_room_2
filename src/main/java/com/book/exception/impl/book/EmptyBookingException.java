package com.book.exception.impl.book;

import com.book.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class EmptyBookingException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "예약건이 없습니다.";
    }
}
