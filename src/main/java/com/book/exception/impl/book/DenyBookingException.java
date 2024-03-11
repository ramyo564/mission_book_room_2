package com.book.exception.impl.book;

import com.book.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class DenyBookingException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {

        return "해당 시간에는 예약이 꽉 찼습니다... 다른 시간 예약부탁 드립니다.";
    }
}
