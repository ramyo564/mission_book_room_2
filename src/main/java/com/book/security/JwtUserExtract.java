package com.book.security;

import com.book.persist.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtUserExtract {

    public static UserEntity currentUser() {
        // 현재 사용자 정보 가져오기
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) authentication.getPrincipal();
    }

}
