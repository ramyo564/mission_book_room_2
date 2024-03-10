package com.book.model;

import com.book.persist.entity.StoreEntity;
import com.book.persist.entity.UserEntity;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;

import static com.book.security.JwtUserExtract.currentUser;


public class Store {
    @Data
    public static class Register {
        private UserEntity owner;
        private String name;
        private String address;
        private String latitude;
        private String longitude;
        private String detail;
        private int table;
        private Timestamp createdAt = Timestamp.from(Instant.now());

        public StoreEntity toEntity(){

            // 로그인한 사용자 (role_owner 정보 가져오기)
            this.owner = currentUser();
            return StoreEntity.builder()
                    .owner(this.owner)
                    .name(this.name)
                    .address(this.address)
                    .latitude(this.latitude)
                    .longitude(this.longitude)
                    .detail(this.detail)
                    .table(this.table)
                    .createdAt(this.createdAt)
                    .build();
        }
    }

    @Data
    public static class Update {
        private UserEntity owner;
        private String name;
        private String address;
        private String latitude;
        private String longitude;
        private String detail;
        private int table;
        private Timestamp updatedAt = Timestamp.from(Instant.now());

        public StoreEntity toEntity(){

            // 로그인한 사용자 (role_owner 정보 가져오기)
            this.owner = currentUser();
            return StoreEntity.builder()
                    .owner(this.owner)
                    .name(this.name)
                    .address(this.address)
                    .latitude(this.latitude)
                    .longitude(this.longitude)
                    .detail(this.detail)
                    .table(this.table)
                    .updatedAt(this.updatedAt)
                    .build();
        }
    }

}
