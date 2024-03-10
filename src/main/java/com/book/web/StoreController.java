package com.book.web;

import com.book.exception.impl.auth.NotAuthException;
import com.book.exception.impl.store.DuplicatedStoreException;
import com.book.model.Store;
import com.book.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/register-store")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<?> registerStore(
            @RequestBody Store.Register request) {

        try {
            var result =
                    this.storeService.register(request);
            return ResponseEntity.ok(result);

        } catch (NotAuthException ex) {
            // role 권한이 없을 때
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());

        } catch (DuplicatedStoreException ex) {
            // 1분 이내 중복 상점 등록시
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());

        }

    }


}
