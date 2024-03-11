package com.book.exception.impl.book;

import com.book.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class OverBookingTimeException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {

        return "예약시간 10분전까지 체크인이 되지 않아서 예약이 취소되었습니다.";
    }
}