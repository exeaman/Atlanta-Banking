package com.atlanta.banking.identity.service.utils.seeder;

import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.entity.Role;
import com.atlanta.banking.identity.service.repository.EmployeeRepository;
import com.atlanta.banking.identity.service.repository.RoleRepository;
import com.atlanta.banking.identity.service.utils.generator.EmployeeIdGenerator;
import com.atlanta.banking.identity.service.utils.generator.UsernameGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class EmployeeSeeder {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final EmployeeIdGenerator employeeIdGenerator;
    private final UsernameGenerator usernameGenerator;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void seed() {

        if (employeeRepository.count() > 0) {
            return;
        }

        List<Employee> employees = new ArrayList<>();

        for (SeedEmployee seedEmployee : SeedData.INITIAL_EMPLOYEES) {

            Employee employee = Employee.builder()
                    .employeeId(employeeIdGenerator.generate())
                    .username(generateUniqueUsername(
                            seedEmployee.firstName(),
                            seedEmployee.lastName()))
                    .password(passwordEncoder.encode(seedEmployee.password()))
                    .firstName(seedEmployee.firstName())
                    .lastName(seedEmployee.lastName())
                    .email(seedEmployee.email())
                    .phoneNumber(seedEmployee.phoneNumber())
                    .department(seedEmployee.department())
                    .designation(seedEmployee.designation())
                    .credentialsExpired(false)
                    .roles(loadRoles(seedEmployee.roles()))
                    .build();

            employees.add(employee);
        }

        employeeRepository.saveAll(employees);
    }

    private Set<Role> loadRoles(Set<String> roleNames) {

        List<Role> roles = roleRepository.findAllByNameIn(roleNames);

        if (roles.size() != roleNames.size()) {
            throw new IllegalStateException("One or more seed roles are missing.");
        }

        return new HashSet<>(roles);
    }

    private String generateUniqueUsername(String firstName, String lastName) {

        String username;

        do {
            username = usernameGenerator.generate(firstName, lastName);
        } while (employeeRepository.existsByUsername(username));

        return username;
    }
}