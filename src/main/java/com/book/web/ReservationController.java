package com.book.web;

import com.book.exception.impl.auth.NotAuthException;
import com.book.exception.impl.auth.UnauthorizedAccessException;
import com.book.exception.impl.book.DenyBookingException;
import com.book.exception.impl.book.EmptyBookingException;
import com.book.exception.impl.book.FullBookingException;
import com.book.exception.impl.book.HoldingBookingException;
import com.book.model.Reservation;
import com.book.model.ReservationResult;
import com.book.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/search-table/{storeId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> bookTable(
            @PathVariable Long storeId,
            @RequestBody @Valid Reservation.Booking booking
            ){
        try {
            this.reservationService.bookTable(storeId, booking);
            return ResponseEntity.ok().build();
        }catch (FullBookingException ex){
            // 예약 불가 예외 처리
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        }

    }
    @GetMapping("/check/owner")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<?> checkBooking(
    ){
        try {
            // 예약 건 확인하기
            return ResponseEntity.ok().body(
                    this.reservationService.checkBookingOwner());
        }catch (EmptyBookingException ex){
            // 예약건이 없을 때 예외 처리
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        }

    }

    @PostMapping("/check/owner/{reservationId}")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<?> resultBooking(
            @PathVariable Long reservationId,
            @RequestBody ReservationResult reservationResult
            ){
        try {
            // 예약 건 확인하기
            this.reservationService.resultBooking(
                    reservationResult, reservationId);
            return ResponseEntity.ok().build();

        }catch (EmptyBookingException ex){
            // 예약건이 없을 때 예외 처리
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        }catch (NotAuthException ex){
            // 식당 주인이 아닐 때 예외 처리
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        }

    }

    @GetMapping("/check/customer/{reservationId}")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER','ROLE_OWNER')")
    public ResponseEntity<?> checkBookingCustomer(
            @PathVariable Long reservationId
    ){
        try{
            // 예약 건 확인하기 & 도착 확인 기능 구현
            Reservation.CheckBookingOwner bookingResult =
                    this.reservationService.checkBookingCustomer(reservationId);
            return ResponseEntity.ok().body(bookingResult);
        }catch (UnauthorizedAccessException ex){
            // 본인 확인 예외 처리
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        }catch (EmptyBookingException ex){
            // 예약건이 없을 경우 예외처리
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        }catch (HoldingBookingException ex){
            // 아직 holding 중일 때
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        }catch (DenyBookingException ex){
            // 예약 불가 예외 처리
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        }

    }

    @PostMapping("/check/customer/{reservationId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> checkArrivedCustomer(
            @PathVariable Long reservationId
    ){
        // 도착 정보 알리기
        this.reservationService.arrivedChecking(reservationId);
        // 매장에서 로그인해서 처리 -> 예약번호 이외에 다른 정보 필요 X

        return ResponseEntity.ok().build();
    }


}
