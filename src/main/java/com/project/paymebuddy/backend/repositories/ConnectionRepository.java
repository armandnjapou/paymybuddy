package com.project.paymebuddy.backend.repositories;

import com.project.paymebuddy.backend.entities.Connection;
import com.project.paymebuddy.backend.entities.PayMyBuddyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    Set<Connection> findBySender(PayMyBuddyUser pmb);

    Set<Connection> findByTarget(PayMyBuddyUser pmb);
}
