package com.book.persist.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "stores")
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;
    @Column
    private String address;
    @Column
    private String latitude;
    @Column
    private String longitude;
    @Column
    private String detail;
    @Column
    private Timestamp createdAt;
    @Column
    private Timestamp updatedAt;

    @Column
    private boolean deleted;
    @Column
    private Timestamp deletedAt;

    @ManyToOne
    @JoinColumn(name = "phoneNumber")
    private UserEntity owner;

}
