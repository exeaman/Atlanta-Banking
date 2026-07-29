package com.atlanta.banking.accounts.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "account_number_sequence")
public class AccountNumberSequence {
    @Id
    private Long id;
    @Column(name = "next_val", nullable = false)
    private Long nexVal;
}
