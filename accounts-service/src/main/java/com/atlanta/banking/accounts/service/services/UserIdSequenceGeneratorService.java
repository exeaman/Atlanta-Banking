package com.atlanta.banking.accounts.service.services;

import org.springframework.stereotype.Service;

import com.atlanta.banking.accounts.service.entity.UserIdSequence;
import com.atlanta.banking.accounts.service.repository.UserIdSequenceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserIdSequenceGeneratorService {
    private final UserIdSequenceRepository repo;

    public String generateUserId(){
        UserIdSequence seq = repo.lockSequence();
        long value = seq.getNextVal();

        seq.setNextVal(value+1);

        repo.save(seq);
        return String.format("USER%06d", value);
    }
}
