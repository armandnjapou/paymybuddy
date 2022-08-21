package com.project.paymebuddy.backend.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "operation")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Account account;

    @Column(name = "initiator_id")
    private Long initiatorId;

    @Column(name = "target_id")
    private Long targetId;

    public Pair<Account, Account> getTransferAccounts() {
        return null;
    }
}
