package com.project.paymebuddy.backend.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "connection")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    PayMyBuddyUser sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    PayMyBuddyUser target;

    @Column(name = "connection_date")
    private LocalDateTime connectionDate;
}
