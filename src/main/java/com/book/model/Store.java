package com.book.model;

import com.book.persist.entity.StoreEntity;
import com.book.persist.entity.UserEntity;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.sql.Timestamp;
import java.time.Instant;


public class Store {
    @Data
    public static class Register {
        private UserEntity owner;
        private String name;
        private String address;
        private String latitude;
        private String longitude;
        private String detail;
        private Timestamp createdAt = Timestamp.from(Instant.now());

        public StoreEntity toEntity(){
            // 현재 사용자 정보 가져오기
            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();
            UserEntity currentUser =
                    (UserEntity) authentication.getPrincipal();
            // 로그인한 사용자 (role_owner 정보 가져오기)
            this.owner = currentUser;
            return StoreEntity.builder()
                    .owner(this.owner)
                    .name(this.name)
                    .address(this.address)
                    .latitude(this.latitude)
                    .longitude(this.longitude)
                    .detail(this.detail)
                    .createdAt(this.createdAt)
                    .build();
        }
    }
}
