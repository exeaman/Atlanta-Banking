package com.atlanta.banking.identity.service.utils.seeder;

import com.atlanta.banking.identity.service.entity.EmployeeSequence;
import com.atlanta.banking.identity.service.repository.EmployeeSequenceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeSequenceSeeder {

    private final EmployeeSequenceRepository repository;

    @Transactional
    public void seed() {

        if (repository.existsById(1L)) {
            return;
        }

        repository.save(
                new EmployeeSequence(
                        1L,
                        100000000L
                )
        );
    }
}