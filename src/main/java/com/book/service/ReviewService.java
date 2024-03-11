package com.book.service;

import com.book.exception.impl.auth.UnauthorizedAccessException;
import com.book.exception.impl.book.EmptyBookingException;
import com.book.exception.impl.book.NotReviewAuthException;
import com.book.model.Review;
import com.book.persist.ReservationRepository;
import com.book.persist.ReviewRepository;
import com.book.persist.entity.ReservationEntity;
import com.book.persist.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.book.security.JwtUserExtract.currentUser;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    public ReviewEntity writeReview(
            Long reservationId, Review.Writing reviewWriting) {
        // 예약정보 갖고 오기 (예약 승인 및 실제로 도착한 경우)
        ReservationEntity reservation =
                reservationRepository
                        .findByIdAndApprovedIsTrueAndArrivedIsTrue(
                                reservationId)
                        .orElseThrow(NotReviewAuthException::new);

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
                .createdAt(reviewWriting.getTime())
                .build();


        return this.reviewRepository.save(review);
    }

    public ReviewEntity updateReview(
            Long reviewId, Review.Writing reviewWriting
    ) {

        // 리뷰 갖고 오기
        ReviewEntity review =
                reviewRepository
                        .findById(reviewId)
                        .orElseThrow(EmptyBookingException::new);

        // 해당 리뷰 작성자 확인
        String currentCustomerPhoneNumber = currentUser().getPhoneNumber();
        log.info("현재 로그인한 고객 정보 " + currentCustomerPhoneNumber);
        log.info(
                "예약자 고객 정보 " + review.getUser()
                        .getPhoneNumber());

        // 작성자와 일치하지 않을 경우 예외 처리
        if (!currentCustomerPhoneNumber
                .equals(review.getUser().getPhoneNumber())) {
            throw new UnauthorizedAccessException();
        }

        // 리뷰 수정 -> 내용, 별점, 업데이트 날짜
        review.setContent(reviewWriting.getContent());
        review.setRating(reviewWriting.getRating());
        review.setUpdateAt(reviewWriting.getTime());

        return this.reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        // 리뷰 갖고 오기
        ReviewEntity review =
                reviewRepository
                        .findById(reviewId)
                        .orElseThrow(EmptyBookingException::new);

        // 해당 리뷰 작성자 확인
        String currentCustomerPhoneNumber = currentUser().getPhoneNumber();

        log.info("현재 로그인한 고객 정보 " + currentCustomerPhoneNumber);
        log.info(
                "예약자 고객 정보 " + review.getStore().getOwner()
                        .getPhoneNumber());

        // 작성자 혹은 해당 매장 오너와 일치하지 않을 경우 예외 처리
        if (currentCustomerPhoneNumber
                .equals(review.getUser().getPhoneNumber())) {

            review.setDeleted(true);
            review.setDeletedAt(LocalDateTime.now());
            this.reviewRepository.save(review);

        } else if (currentCustomerPhoneNumber
                .equals(review.getStore().getOwner().getPhoneNumber())
        ) {
            // 삭제로직
            review.setDeleted(true);
            review.setDeletedAt(LocalDateTime.now());
            this.reviewRepository.save(review);

        } else {
            throw new UnauthorizedAccessException();
        }


    }
}
