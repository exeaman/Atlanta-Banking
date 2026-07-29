package com.atlanta.banking.accounts.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user_id_sequence")
public class UserIdSequence {
    @Id
    private Long id;

    @Column(name = "next_val", nullable = false)
    private Long nextVal;
}
