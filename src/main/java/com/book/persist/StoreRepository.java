package com.book.persist;

import com.book.persist.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;

public interface StoreRepository
        extends JpaRepository<StoreEntity, Long> {
    int countByNameAndAddressAndCreatedAtIsGreaterThanEqual(String name, String address, Timestamp createdAt);
}
