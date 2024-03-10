package com.book.service;

import com.book.exception.impl.auth.NotAuthException;
import com.book.exception.impl.store.DuplicatedStoreException;
import com.book.model.Store;
import com.book.persist.StoreRepository;
import com.book.persist.UserRepository;
import com.book.persist.entity.StoreEntity;
import com.book.persist.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.book.security.JwtUserExtract.currentUser;

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

        // 현재 사용자의 인증 정보 가져오기
        UserEntity currentUser = currentUser();
        // 권한이 없는 일반 고객일 경우
        List<String> roles = currentUser.getRoles();
        if (!roles.contains("ROLE_OWNER")) {
            //예외 작동 안함..
            throw new NotAuthException();
        }

        StoreEntity result = this.storeRepository.save(request.toEntity());

        return result;
    }




}
