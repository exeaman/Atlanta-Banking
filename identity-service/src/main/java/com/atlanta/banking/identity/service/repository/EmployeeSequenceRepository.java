package com.atlanta.banking.identity.service.repository;

import com.atlanta.banking.identity.service.entity.EmployeeSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeSequenceRepository extends JpaRepository<EmployeeSequence, Long> {
}