package com.atlanta.banking.accounts.service.services;

import org.springframework.stereotype.Service;

import com.atlanta.banking.accounts.service.entity.AccountNumberSequence;
import com.atlanta.banking.accounts.service.repository.AccountNumberSequenceRepository;
import com.atlanta.banking.accounts.service.utils.AccountType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountNumberSequenceGeneratorService {
    private static final String BANK_CODE = "2026";

    private final AccountNumberSequenceRepository repo;

    public String generateAccountNumber(AccountType accountType) {
        AccountNumberSequence seq = repo.lockSequence();

        long value = seq.getNexVal();

        seq.setNexVal(value+1);

        repo.save(seq);

        String typeCode = switch (accountType){
            case SAVINGS -> "01";
            case CURRENT -> "02";
        };

        return BANK_CODE + typeCode + String.format("%09d", value);
    }
}
