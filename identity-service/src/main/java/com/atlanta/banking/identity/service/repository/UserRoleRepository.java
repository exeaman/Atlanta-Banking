package com.atlanta.banking.identity.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atlanta.banking.identity.service.entity.UserRoles;

public interface UserRoleRepository extends JpaRepository<UserRoles, Long>{
    
}
