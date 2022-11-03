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
    @Column(name = "pay_my_buddy_user_id")
    private Long id;

    private String name;
    private String username;

    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "sender", targetEntity = Connection.class)
    private Set<PayMyBuddyUser> connections = new HashSet<>();

    @OneToMany(mappedBy = "target", targetEntity = Connection.class)
    private Set<PayMyBuddyUser> connectedTo = new HashSet<>();

/*    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "user_connection",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "connection_id"))
    private Set<PayMyBuddyUser> connections = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "user_connection",
            joinColumns = @JoinColumn(name = "connection_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<PayMyBuddyUser> connectedTo = new HashSet<>();*/



    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
