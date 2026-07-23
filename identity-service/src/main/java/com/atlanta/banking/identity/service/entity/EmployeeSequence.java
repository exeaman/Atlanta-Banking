package com.atlanta.banking.identity.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee_sequence")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSequence {

    @Id
    private Long id = 1L;

    @Column(nullable = false)
    private Long nextValue;
}