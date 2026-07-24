package com.atlanta.banking.identity.service.security;

import com.atlanta.banking.identity.service.entity.Employee;
import com.atlanta.banking.identity.service.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Employee employee = employeeRepository.findWithRolesByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username or password." + username));

        return new CustomUserDetails(employee);
    }
}