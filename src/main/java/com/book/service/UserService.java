package com.book.service;

import com.book.exception.impl.auth.AlreadyExistUserException;
import com.book.exception.impl.auth.NoUserException;
import com.book.exception.impl.auth.WrongPasswordException;
import com.book.model.AuthUser;
import com.book.model.Store;
import com.book.persist.StoreRepository;
import com.book.persist.UserRepository;
import com.book.persist.entity.StoreEntity;
import com.book.persist.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private StoreRepository storeRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber)
            throws UsernameNotFoundException {
        return this.userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(()-> new UsernameNotFoundException(
                        "등록되지 않은 회원입니다. -> " + phoneNumber
                ));
    }
    public UserEntity register(AuthUser.SignUp member){
        boolean exists =
                this.userRepository.existsByPhoneNumber(
                        member.getPhoneNumber());
        if (exists) {
            throw new AlreadyExistUserException();
        }
        member.setPassword(
                this.passwordEncoder.encode(member.getPassword()));
        var result =
                this.userRepository.save(member.toEntity());
        return result;
    }

    public UserEntity authenticate(AuthUser.SignIn member) {
        var user =
                this.userRepository.findByPhoneNumber(
                                member.getPhoneNumber())
                        .orElseThrow(NoUserException::new);
        if (!this.passwordEncoder.matches(
                member.getPassword(),
                user.getPassword())
            )
        {
            throw new WrongPasswordException();
        }

        return user;
    }

}