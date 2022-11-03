package com.project.paymebuddy.backend.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "connection")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "connection_id")
    private Long id;

    private Long targetId;

    @Column(name = "connection_date")
    private LocalDateTime connectionDate;
}
