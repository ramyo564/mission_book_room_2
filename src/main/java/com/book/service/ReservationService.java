package com.book.service;

import com.book.exception.impl.auth.NotAuthException;
import com.book.exception.impl.auth.UnauthorizedAccessException;
import com.book.exception.impl.book.*;
import com.book.exception.impl.store.NotRegisteredStoreException;
import com.book.model.Reservation;
import com.book.model.ReservationResult;
import com.book.persist.BookingStatus;
import com.book.persist.ReservationRepository;
import com.book.persist.StoreRepository;
import com.book.persist.entity.ReservationEntity;
import com.book.persist.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.book.security.JwtUserExtract.currentUser;

@Slf4j
@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;

    public ReservationEntity bookTable(
            Long storeId,
            Reservation.Booking booking) {

        // 점포 확인
        StoreEntity store
                = storeRepository.findById(storeId)
                .orElseThrow(NotRegisteredStoreException::new);

        log.info(store.getName() + " 점포 확인");

        // 매장의 총 테이블 갯수
        int totalTable = store.getTotalTable();
        log.info(totalTable + " 테이블 갯수");

        // 승인 받은 테이블 갯수
        long countTable = reservationRepository
                .countByDateAndTimeAndApprovedIsTrue(
                        booking.getDate(), booking.getTime());
        log.info(countTable + "승인 받은 테이블 갯수");

        if (totalTable > countTable) {
            // 예약 가능 -> 매장 테이블 갯수보다 승인된 테이블 갯수가 적어야지 예약 가능
            ReservationEntity temp = booking.toEntity();
            temp.setStoreName(store);

            return reservationRepository.save(temp);
        } else {
            //예약 불가
            throw new FullBookingException();
        }

    }

    public List<Reservation.CheckBookingOwner> checkBookingOwner(
    ) {

        // 매장 주인 확인 및 예약건 불러오기
        List<ReservationEntity> pendingReservations = reservationRepository
                .findByStoreName_Owner_IdAndApprovedIsFalse(
                        currentUser().getId())
                .orElseThrow(EmptyBookingException::new);


        // API 정리
        List<Reservation.CheckBookingOwner> reservations = new ArrayList<>();

        for (ReservationEntity reservation : pendingReservations) {
            Reservation.CheckBookingOwner ownerChecking =
                    new Reservation.CheckBookingOwner(reservation);
            reservations.add(ownerChecking);
        }

        return reservations;

    }

    public void resultBooking(
            ReservationResult result, Long reservationId) {
        // 예약 건 검색
        ReservationEntity reservation =
                reservationRepository.findById(reservationId)
                        .orElseThrow(EmptyBookingException::new);

        log.info(reservation.toString() + " 예약건 확인");

        // 토큰정보와 일치하지 않을 경우 예외 처리
        if (!reservation.getStoreName().getOwner().getPhoneNumber()
                .equals(currentUser().getPhoneNumber())) {
            throw new NotAuthException();
        }

        // 매장 주인으로부터 승인 받았을 경우 처리 로직
        if (result.isApproved()) {
            reservation.setApproved(true);
            reservation.setStatus(BookingStatus.APPROVE);
            reservationRepository.save(reservation);

        } else {
            reservation.setStatus(BookingStatus.DENY);
            reservationRepository.save(reservation);
        }
    }

    public Reservation.CheckBookingOwner checkBookingCustomer(
            Long reservationId) {
        // 예약 건 불러오기
        ReservationEntity reservation =
                reservationRepository
                        .findById(reservationId)
                        .orElseThrow(EmptyBookingException::new);

        //본인 확인
        String currentCustomerPhoneNumber =
                currentUser().getPhoneNumber();

        log.info("현재 로그인한 고객 정보 " + currentCustomerPhoneNumber);
        log.info("예약자 고객 정보 " +
                reservation.getCustomerName().getPhoneNumber());

        if (!currentCustomerPhoneNumber
                .equals(reservation.getCustomerName().getPhoneNumber())) {
            throw new UnauthorizedAccessException();
        }

        if (!reservation.isApproved()) {
            if (reservation.getStatus().equals(BookingStatus.DENY)) {
                // 오너가 예약 거절시
                throw new DenyBookingException();

            } else if (reservation.getStatus()
                    .equals(BookingStatus.HOLDING)) {
                // 아직 holding 일 때
                throw new HoldingBookingException();
            }

        }
        return new Reservation.CheckBookingOwner(reservation);

    }

    public void arrivedChecking(Long reservationId) {
        // 예약 건 불러오기
        ReservationEntity reservation =
                reservationRepository
                        .findById(reservationId)
                        .orElseThrow(EmptyBookingException::new);

        //본인 확인
        String currentCustomerPhoneNumber = currentUser().getPhoneNumber();

        if (!currentCustomerPhoneNumber
                .equals(reservation.getCustomerName().getPhoneNumber())) {
            throw new UnauthorizedAccessException();
        }

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();

        // 예약 시간
        LocalDateTime reservationDateTime =
                reservation.getDate().atTime(
                        reservation.getTime(), 0);

        // 예약 시간에서 10분을 뺀 시간
        LocalDateTime tenMinutesBeforeReservation =
                reservationDateTime.minus(
                        10, ChronoUnit.MINUTES);

        // 현재 시간이 예약 시간보다 10분 전인지 확인
        if (!now.isBefore(tenMinutesBeforeReservation)) {
            // 도착시 10분이 넘어가면 거부
            reservation.setStatus(BookingStatus.DENY);
            this.reservationRepository.save(reservation);
            throw new OverBookingTimeException();
        }

        // 예외처리
        if (!reservation.isApproved()) {
            if (reservation.getStatus().equals(BookingStatus.DENY)) {
                // 오너가 예약 거절시
                throw new DenyBookingException();
            } else if (reservation.getStatus().equals(BookingStatus.HOLDING)) {
                // 아직 holding 일 때
                throw new HoldingBookingException();
            }

        }
        // 도착
        reservation.setArrived(true);
        reservation.setArrivedTime(LocalDateTime.now());
        reservationRepository.save(reservation);
    }


}
