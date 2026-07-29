package com.atlanta.banking.accounts.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.atlanta.banking.accounts.service.entity.UserIdSequence;

import jakarta.persistence.LockModeType;

public interface UserIdSequenceRepository extends JpaRepository<UserIdSequence,Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from UserIdSequence s where s.id = 1")
    UserIdSequence lockSequence();
}
