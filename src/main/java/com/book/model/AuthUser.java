package com.book.model;

import com.book.persist.entity.UserEntity;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class AuthUser {

    @Data
    public static class SignIn {
        private String phoneNumber;
        private String password;
    }

    @Data
    public static class SignUp{
        private String phoneNumber;
        private String password;
        private Timestamp createdAt = Timestamp.from(Instant.now());
        private List<String> roles;

        public UserEntity toEntity(){
            return UserEntity.builder()
                    .phoneNumber(this.phoneNumber)
                    .password(this.password)
                    .createdAt(this.createdAt)
                    .roles(this.roles)
                    .build();
        }
    }
}