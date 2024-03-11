package com.book.persist.entity;

import com.book.persist.BookingStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "reservations")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private UserEntity customerName;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private StoreEntity storeName;

    @Column
    private int people;

    @Column
    private LocalDate date;

    @Column
    private int time;

    @Column
    private boolean approved;

    @Column
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column
    private boolean arrived;

    @Column
    private LocalDateTime arrivedTime;

}
