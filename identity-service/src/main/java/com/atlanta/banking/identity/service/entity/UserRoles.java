package com.atlanta.banking.identity.service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_roles", schema = "identity")
@Data
public class UserRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;
    @Column(nullable = false)
    private String role; // Prefix [Role_.............]
}
