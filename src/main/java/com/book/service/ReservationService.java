package com.book.service;

import com.book.exception.impl.auth.NotAuthException;
import com.book.exception.impl.book.EmptyBookingException;
import com.book.exception.impl.book.FullBookingException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                        booking.getDate(),booking.getTime());
        log.info(countTable + "승인 받은 테이블 갯수");

        if (totalTable > countTable) {
            // 예약 가능
            ReservationEntity temp = booking.toEntity();
            temp.setStoreName(store);

            return reservationRepository.save(temp);
        } else {
            //예약 불가
            throw new FullBookingException();
        }

    }

    public List<Reservation.CheckBookingOwner> checkBookingOwner(
    ){

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
            ReservationResult result, Long reservationId){
        // 예약 건 검색
        ReservationEntity reservation =
                reservationRepository.findById(reservationId)
                        .orElseThrow(EmptyBookingException::new);
        log.info(reservation.toString() + " 예약건 확인");
        if(!reservation.getStoreName().getOwner().getPhoneNumber()
                .equals(currentUser().getPhoneNumber())){
            throw new NotAuthException();
        }


        if(result.isApproved()){
            reservation.setApproved(true);
            reservation.setStatus(BookingStatus.APPROVE);
            reservationRepository.save(reservation);

        }else {
            reservation.setStatus(BookingStatus.DENY);
            reservationRepository.save(reservation);
        }
    }
}
