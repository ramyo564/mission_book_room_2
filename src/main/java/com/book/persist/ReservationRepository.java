package com.book.persist;

import com.book.persist.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ReservationRepository
        extends JpaRepository<ReservationEntity, Long> {

    long countByDateAndTimeAndApprovedIsTrue(LocalDate date, int time);
}
