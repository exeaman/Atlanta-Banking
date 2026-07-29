package com.atlanta.banking.identity.service.utils.seeder;

import com.atlanta.banking.identity.service.entity.Role;
import com.atlanta.banking.identity.service.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleSeeder {

    private final RoleRepository roleRepository;

    @Transactional
    public void seed() {

        if (roleRepository.count() > 0) {
            return;
        }

        List<Role> roles = new ArrayList<>();

        for (String[] roleData : SeedData.INITIAL_ROLES) {

            roles.add(Role.builder().name(roleData[0]).description(roleData[1]).build());
        }

        roleRepository.saveAll(roles);
    }
}