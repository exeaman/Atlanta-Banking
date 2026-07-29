package com.atlanta.banking.identity.service.utils.seeder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevDataSeeder implements CommandLineRunner {

    private final RoleSeeder roleSeeder;
    private final EmployeeSequenceSeeder employeeSequenceSeeder;
    private final EmployeeSeeder employeeSeeder;

    @Override
    public void run(String... args) {
        roleSeeder.seed();
        employeeSequenceSeeder.seed();
        employeeSeeder.seed();
    }
}