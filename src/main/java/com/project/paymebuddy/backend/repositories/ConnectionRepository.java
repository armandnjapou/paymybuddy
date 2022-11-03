package com.project.paymebuddy.backend.repositories;

import com.project.paymebuddy.backend.entities.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
}
