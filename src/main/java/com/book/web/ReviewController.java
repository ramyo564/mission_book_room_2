package com.book.web;

import com.book.exception.impl.auth.UnauthorizedAccessException;
import com.book.exception.impl.book.EmptyBookingException;
import com.book.exception.impl.book.NotReviewAuthException;
import com.book.model.Review;
import com.book.persist.entity.ReviewEntity;
import com.book.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/write/{reservationId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> writeReview(
            @PathVariable Long reservationId,
            @RequestBody Review.Writing reviewWriting
    ) {
        try {
            // 리뷰 작성 (작성자)
            ReviewEntity reviewEntity =
                    this.reviewService
                            .writeReview(reservationId, reviewWriting);
            return ResponseEntity.ok().build();
        } catch (NotReviewAuthException ex) {
            // 리뷰 예외처리
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        } catch (
                UnauthorizedAccessException ex) {
            // 본인 확인 예외 처리
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        }
    }

    @PatchMapping("/update/{reservationId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reservationId,
            @RequestBody Review.Writing reviewWriting
    ) {
        try {
            // 리뷰 수정 (작성자)
            this.reviewService.updateReview(reservationId, reviewWriting);
            return ResponseEntity.ok().build();

        } catch (EmptyBookingException ex) {
            // 예약건 예외처리
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        } catch (
                UnauthorizedAccessException ex) {
            // 본인 확인 예외 처리
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        }
    }

    @DeleteMapping("/delete/{reviewId}")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER','ROLE_OWNER')")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId) {
        try {
            // 리뷰 삭제 (작성자, 관리자)
            this.reviewService.deleteReview(reviewId);
            return ResponseEntity.ok().build();
        } catch (EmptyBookingException ex) {
            // 예약건 예외처리
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        } catch (
                UnauthorizedAccessException ex) {
            // 본인 확인 예외 처리
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        }

    }


}
