package com.atlanta.banking.identity.service.utils.generator;

import com.atlanta.banking.identity.service.entity.EmployeeSequence;
import com.atlanta.banking.identity.service.repository.EmployeeSequenceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeIdGenerator {

    private final EmployeeSequenceRepository sequenceRepository;

    @Transactional
    public String generate() {

        EmployeeSequence sequence = sequenceRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Employee sequence not initialized."));

        long employeeId = sequence.getNextValue();

        sequence.setNextValue(employeeId + 1);

        sequenceRepository.save(sequence);

        return String.valueOf(employeeId);
    }
}