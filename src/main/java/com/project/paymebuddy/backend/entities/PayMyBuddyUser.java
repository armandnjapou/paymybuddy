package com.project.paymebuddy.backend.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Entity
@Table(name = "pay_my_buddy_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        })
public class PayMyBuddyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_my_buddy_user_id")
    private Long id;

    private String username;
    private String password;

    @OneToMany(mappedBy = "payMyBuddyUser")
    private List<Connection> myConnections;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
