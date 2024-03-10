package com.book.service;

import com.book.exception.impl.store.DuplicatedStoreException;
import com.book.model.Store;
import com.book.persist.StoreRepository;
import com.book.persist.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreEntity register(Store.Register request) {
        // 같은 건물에 여러개 상점이 있을 수도 있어서 중복검사 안함
        // 1분 안에는 중복 등록만 안되게 설정
        LocalDateTime checkTime =
                LocalDateTime.now().minusMinutes(1);
        int checkDuplicator = storeRepository
                .countByNameAndAddressAndCreatedAtIsGreaterThanEqual(
                        request.getName(),
                        request.getAddress(),
                        Timestamp.valueOf(checkTime));

        if(checkDuplicator > 0){
            throw new DuplicatedStoreException();
        }
        StoreEntity result = this.storeRepository.save(request.toEntity());

        return result;
    }




}
