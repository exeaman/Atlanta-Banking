package com.atlanta.banking.identity.service.utils.seeder;

import com.atlanta.banking.identity.service.enums.Department;
import com.atlanta.banking.identity.service.enums.Designation;

import java.util.Set;

public record SeedEmployee(

        String firstName, String lastName, String email, String phoneNumber,

        Department department, Designation designation,

        String password,

        Set<String> roles

) {
}