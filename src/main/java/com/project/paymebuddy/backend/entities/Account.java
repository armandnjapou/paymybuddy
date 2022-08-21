package com.project.paymebuddy.backend.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal balance;

    @OneToMany(mappedBy = "account")
    private List<Operation> operations;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name= "user_id")
    private PayMyBuddyUser user;

    public Account(BigDecimal balance) {
        this.balance = balance;
    }

    public Account(BigDecimal balance, LocalDateTime creationDate) {
        this.balance = balance;
        this.createdAt = creationDate;
    }
}
