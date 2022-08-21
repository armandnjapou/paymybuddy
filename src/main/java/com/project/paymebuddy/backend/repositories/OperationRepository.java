package com.project.paymebuddy.backend.repositories;

import com.project.paymebuddy.backend.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Integer> {
}
