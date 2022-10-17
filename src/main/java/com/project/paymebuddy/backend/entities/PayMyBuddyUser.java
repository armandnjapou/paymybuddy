package com.project.paymebuddy.backend.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "pay_my_buddy_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
public class PayMyBuddyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_my_buddy_user_id")
    private Long id;

    private String email;
    private String password;

    @OneToMany(mappedBy = "payMyBuddyUser")
    private List<Connection> myConnections;
}
