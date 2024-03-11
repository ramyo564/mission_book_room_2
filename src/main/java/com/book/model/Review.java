package com.book.model;

import com.book.persist.BookingStatus;
import com.book.persist.entity.ReservationEntity;
import com.book.persist.entity.StoreEntity;
import com.book.persist.entity.UserEntity;
import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.book.security.JwtUserExtract.currentUser;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class Review {
    @Data
    public static class Writing {


        @Size(min = 2, message = "두 글자 이상 입력해주세요")
        private String reviewer;

        @Size(min = 10, message = "10 글자 이상 입력해주세요")
        private String content;

        @Min(value = 1, message = "1점 이상 입력해 주세요")
        @Max(value = 5, message = "5점 이하로 입력해 주세요")
        private int rating;

        private LocalDateTime time = LocalDateTime.now();

    }
}
