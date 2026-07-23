package com.atlanta.banking.identity.service.repository;

import com.atlanta.banking.identity.service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    List<Role> findAllByNameIn(Set<String> names);

    boolean existsByName(String name);
}