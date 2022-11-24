package com.project.paymebuddy.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "pay_my_buddy_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        })
public class PayMyBuddyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @OneToMany
    @JoinColumn(name = "sender")
    private Set<PayMyBuddyUser> connections = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "target")
    @JsonIgnore
    private Set<PayMyBuddyUser> connectedTo = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private Set<Role> roles;

    public void addConnection(PayMyBuddyUser target) {
        this.connections.add(target);
    }
}
