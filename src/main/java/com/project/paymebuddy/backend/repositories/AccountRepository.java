package com.project.paymebuddy.backend.repositories;

import com.project.paymebuddy.backend.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
