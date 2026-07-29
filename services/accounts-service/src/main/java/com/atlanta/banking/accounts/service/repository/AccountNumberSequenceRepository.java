package com.atlanta.banking.accounts.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.atlanta.banking.accounts.service.entity.AccountNumberSequence;

import jakarta.persistence.LockModeType;

public interface AccountNumberSequenceRepository extends JpaRepository<AccountNumberSequence, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from AccountNumberSequence s where s.id = 1")
    AccountNumberSequence lockSequence();
}