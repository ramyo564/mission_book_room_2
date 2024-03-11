package com.book.exception.impl.book;

import com.book.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotReviewAuthException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {

        return "서비스를 실제 방문해서 이용하기 전까지는 리뷰를 작성할 수 없습니다.";
    }
}