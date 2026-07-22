package com.atlanta.banking.identity.service.repository;

import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    @EntityGraph(attributePaths = "roles")
    Optional<Employee> findWithRolesByUsername(String username);

    boolean existsByName(String name);
}