package com.project.paymebuddy.backend.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "pay_my_buddy_user")
public class PayMyBuddyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @ManyToOne
    private PayMyBuddyUser parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<PayMyBuddyUser> connectionList;
}
