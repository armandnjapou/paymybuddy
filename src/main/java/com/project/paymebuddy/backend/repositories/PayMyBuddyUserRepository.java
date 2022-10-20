package com.project.paymebuddy.backend.repositories;

import com.project.paymebuddy.backend.entities.PayMyBuddyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayMyBuddyUserRepository extends JpaRepository<PayMyBuddyUser, Long> {
    Optional<PayMyBuddyUser> findByUsername(String username);
}
