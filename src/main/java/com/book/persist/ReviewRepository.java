package com.book.persist;

import com.book.persist.entity.ReservationEntity;
import com.book.persist.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository
        extends JpaRepository<ReviewEntity, Long> {
}
