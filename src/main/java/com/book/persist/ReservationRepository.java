package com.book.persist;

import com.book.persist.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository
        extends JpaRepository<ReservationEntity, Long> {

    long countByDateAndTimeAndApprovedIsTrue(LocalDate date, int time);

    Optional<List<ReservationEntity>> findByStoreName_Owner_IdAndApprovedIsFalse(Long id);


}
