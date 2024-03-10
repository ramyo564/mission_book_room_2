package com.book.web;

import com.book.exception.impl.auth.NotAuthException;
import com.book.exception.impl.store.AlreadyDeletedStoreException;
import com.book.exception.impl.store.DuplicatedStoreException;
import com.book.exception.impl.store.NotRegisteredStoreException;
import com.book.model.Store;
import com.book.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/register")
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

    @PatchMapping("/update/{store_name}")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<?> updateStore(
            @PathVariable String store_name,
            @RequestBody Store.Update request) {

        try {
            var result =
                    this.storeService.update(request, store_name);
            return ResponseEntity.ok(result);

        } catch (NotAuthException ex) {
            // role 권한이 없을 때
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());

        } catch (DuplicatedStoreException ex) {
            // 1분 이내 중복 상점 등록시
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());

        } catch (NotRegisteredStoreException ex) {
            // 등록되지 않은 상점일 경우
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());

        }


    }

    @DeleteMapping("/delete/{store_name}")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<?> deleteStore(
            @PathVariable String store_name) {

        try {
            this.storeService.delete(store_name);
            return ResponseEntity.ok().build();
        } catch (AlreadyDeletedStoreException ex) {
            // 이미 삭제된 점포일 경우
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        } catch (NotAuthException ex) {
            // role 권한이 없을 때
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());
        } catch (NotRegisteredStoreException ex) {
            // 등록되지 않은 상점일 경우
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getMessage() + ex.getStatusCode());

        }
    }
}