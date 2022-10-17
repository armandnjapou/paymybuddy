package com.project.paymebuddy.backend.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private Long id;

    @OneToOne
    @JoinColumn(name= "pay_my_buddy_user_id")
    private PayMyBuddyUser user;

    private String description;

    private BigDecimal amount;

    @Column(name = "target_id")
    private Long targetId;
}
