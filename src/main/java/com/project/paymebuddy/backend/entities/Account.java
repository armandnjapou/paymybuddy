package com.project.paymebuddy.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "account_id")
    private Long id;

    @Column(nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "account")
    private List<Operation> operations;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name= "user_id")
    @JsonIgnore
    private PayMyBuddyUser user;

    public Account(BigDecimal balance) {
        this.balance = balance;
    }

    public Account(BigDecimal balance, LocalDateTime creationDate) {
        this.balance = balance;
        this.createdAt = creationDate;
    }
}
