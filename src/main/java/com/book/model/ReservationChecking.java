package com.book.model;

import com.book.persist.BookingStatus;
import com.book.persist.entity.ReservationEntity;
import com.book.persist.entity.StoreEntity;
import com.book.persist.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Data
public class ReservationChecking {
    @Data
    public static class CheckBookingOwner{
        private UserEntity customer;
        private StoreEntity store;
        private int people;
        private LocalDate date;
        private int time;
        private BookingStatus status;
        public ReservationEntity toEntity() {

            return ReservationEntity.builder()
                    .customerName(this.customer)
                    .storeName(this.store)
                    .people(this.people)
                    .date(this.date)
                    .time(this.time)
                    .status(BookingStatus.HOLDING)
                    .build();
        }
    }
}
