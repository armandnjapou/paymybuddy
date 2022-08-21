package com.project.paymebuddy.backend.repositories;

import com.project.paymebuddy.backend.entities.PayMyBuddyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayMyBuddyUserRepository extends JpaRepository<PayMyBuddyUser, Integer> {
}
