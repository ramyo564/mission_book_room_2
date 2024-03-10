package com.book.web;

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
            @RequestBody Store.Register request){

        var result =
                this.storeService.register(request);
        return ResponseEntity.ok(result);
    }



}
