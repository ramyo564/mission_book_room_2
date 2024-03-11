package com.book.service;

import com.book.exception.impl.auth.UnauthorizedAccessException;
import com.book.exception.impl.book.EmptyBookingException;
import com.book.model.Review;
import com.book.persist.ReservationRepository;
import com.book.persist.ReviewRepository;
import com.book.persist.entity.ReservationEntity;
import com.book.persist.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.book.security.JwtUserExtract.currentUser;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    public ReviewEntity writeReview(
            Long reservationId, Review.Writing reviewWriting){
        // 리뷰 갖고 오기
        ReservationEntity reservation =
                reservationRepository
                        .findById(reservationId)
                        .orElseThrow(EmptyBookingException::new);

        // 해당 리뷰 작성자 확인
        String currentCustomerPhoneNumber = currentUser().getPhoneNumber();
        log.info("현재 로그인한 고객 정보 " + currentCustomerPhoneNumber);
        log.info(
                "예약자 고객 정보 " + reservation.getCustomerName()
                .getPhoneNumber());

        // 작성자와 일치하지 않을 경우 예외 처리
        if (!currentCustomerPhoneNumber
                .equals(reservation.getCustomerName().getPhoneNumber())) {
            throw new UnauthorizedAccessException();
        }

        // 리뷰 작성
        ReviewEntity review = ReviewEntity.builder()
                .reservation(reservation)
                .store(reservation.getStoreName())
                .user(reservation.getCustomerName())
                .reviewer(reviewWriting.getReviewer())
                .content(reviewWriting.getContent())
                .rating(reviewWriting.getRating())
                .time(reviewWriting.getTime())
                .build();


        return this.reviewRepository.save(review);
    }
}
