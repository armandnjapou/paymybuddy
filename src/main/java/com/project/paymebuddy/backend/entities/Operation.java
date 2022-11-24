package com.project.paymebuddy.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "operation")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    private OperationType operationType;

    private String description;

    private BigDecimal amount;

    @Column(name = "execution_date")
    private LocalDateTime executionDate;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;
}
