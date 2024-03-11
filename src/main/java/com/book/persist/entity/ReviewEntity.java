package com.book.persist.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "reviews")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private ReservationEntity reservation;

    @ManyToOne
    @JoinColumn(name = "review_store_id")
    private StoreEntity store;

    @ManyToOne
    @JoinColumn(name = "review_user_id")
    private UserEntity user;

    @Column
    private String reviewer;

    @Column
    private String content;
    @Column
    private int rating;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updateAt;
    @Column
    private boolean deleted;
    @Column
    private LocalDateTime deletedAt;

}
