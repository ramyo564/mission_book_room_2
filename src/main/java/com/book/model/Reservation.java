package com.book.model;

import com.book.persist.BookingStatus;
import com.book.persist.entity.ReservationEntity;
import com.book.persist.entity.StoreEntity;
import com.book.persist.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.book.security.JwtUserExtract.currentUser;

@AllArgsConstructor
@Builder
@Data
public class Reservation {
    @Data
    public static class Booking {

        private UserEntity customer;

        private StoreEntity store;

        @Min(value = 1, message = "예약 인원은 1명보다 많아야 합니다.")
        private int people;

        @Future(message = "과거의 날짜를 선택할 수 없습니다.")
        private LocalDate date;

        @Min(value = 0, message = "예약 시간은 0~24시 사이 입니다.")
        @Max(value = 24, message = "예약 시간은 0~24시 사이 입니다.")
        private int time;

        private BookingStatus status;

        public ReservationEntity toEntity(){
            this.customer = currentUser();
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

    @Data
    public static class CheckBookingOwner{
        private String storeName;
        private String address;
        private String customerPhoneNumber;
        private int people;
        private LocalDate date;
        private int time;
        private boolean approved;
        private String status;

        public CheckBookingOwner(ReservationEntity reservationEntity) {
            this.storeName = reservationEntity.getStoreName().getName();
            this.address = reservationEntity.getStoreName().getAddress();
            this.customerPhoneNumber = reservationEntity.getCustomerName().getPhoneNumber();
            this.people = reservationEntity.getPeople();
            this.date = reservationEntity.getDate();
            this.time = reservationEntity.getTime();
            this.approved = reservationEntity.isApproved();
            this.status = reservationEntity.getStatus().toString();
        }
    }


}
