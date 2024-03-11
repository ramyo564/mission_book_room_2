package com.book.exception.impl.book;

import com.book.exception.AbstractException;
import org.springframework.http.HttpStatus;


public class HoldingBookingException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {

        return "예약 가능 여부 확인중입니다. 잠시만 기다려주세요";
    }
}