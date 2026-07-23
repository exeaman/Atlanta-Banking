package com.atlanta.banking.identity.service.repository;

import com.atlanta.banking.identity.service.entity.EmployeeSequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeSequenceRepository extends JpaRepository<EmployeeSequence, Long> {
}