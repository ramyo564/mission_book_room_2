package com.book.web;

import com.book.exception.impl.auth.AlreadyExistUserException;
import com.book.exception.impl.auth.NoUserException;
import com.book.exception.impl.auth.WrongPasswordException;
import com.book.model.AuthUser;
import com.book.security.TokenProvider;
import com.book.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;


    @PostMapping("/signup-member")
    public ResponseEntity<?> signupMember(
            @RequestBody AuthUser.SignUp request) {
        // 회원가입을 위한 API
        // 매장주인 및 파트너십 가입은 ROLE 부분에 입력
        try {
            var result =
                    this.userService.register(request);
            return ResponseEntity.ok(result);
        } catch (AlreadyExistUserException ex) {
            // 이미 존재하는 사용자명 예외처리
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        }
    }

    @PostMapping("/signin-member")
    public ResponseEntity<?> signinMember(
            @RequestBody AuthUser.SignIn request) {
        // 로그인을 위한 API
        try {
            var member =
                    this.userService.authenticate(request);

            var token =
                    this.tokenProvider.generateToken(
                            member.getPhoneNumber(),
                            member.getRoles()
                    );
            log.info("user login -> " + request.getPhoneNumber());
            return ResponseEntity.ok(token);
        } catch (NoUserException ex) {
            // 아이디가 없을 경우
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        } catch (WrongPasswordException ex) {
            // 비밀번호가 맞지 않을 경우
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        }


    }


}
