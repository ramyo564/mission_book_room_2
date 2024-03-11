package com.book.model;

import com.book.persist.BookingStatus;
import com.book.persist.entity.ReservationEntity;
import com.book.persist.entity.StoreEntity;
import com.book.persist.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResult {

    private boolean approved;

}
