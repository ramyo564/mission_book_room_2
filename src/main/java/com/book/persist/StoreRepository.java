package com.book.persist;

import com.book.persist.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public interface StoreRepository
        extends JpaRepository<StoreEntity, Long> {
    int countByNameAndAddressAndCreatedAtIsGreaterThanEqual(String name, String address, LocalDateTime createdAt);

    Optional<StoreEntity> findByOwner_PhoneNumberAndName(String phoneNumber, String name);



}
