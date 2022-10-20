package com.project.paymebuddy.backend.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AppRole name;

    public Role(AppRole role) {
        this.name = role;
    }

    public Role() {
    }
}
