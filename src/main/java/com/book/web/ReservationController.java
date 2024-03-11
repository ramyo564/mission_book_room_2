package com.book.web;

import com.book.exception.impl.book.EmptyBookingException;
import com.book.exception.impl.book.FullBookingException;
import com.book.model.Reservation;
import com.book.persist.ReservationRepository;
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
            return ResponseEntity.ok().body(
                    this.reservationService.checkBookingOwner());
        }catch (EmptyBookingException ex){
            // 예약건이 없을 때 예외 처리
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        }

    }






}
