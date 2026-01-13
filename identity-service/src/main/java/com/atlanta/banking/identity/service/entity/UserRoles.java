package com.atlanta.banking.identity.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_roles")
@Data
public class UserRoles {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;
    @Column(nullable = false)
    private String role; // Prefix [Role_.............]
}
