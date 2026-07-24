package com.atlanta.banking.identity.service.entity;

import com.atlanta.banking.identity.service.enums.Department;
import com.atlanta.banking.identity.service.enums.Designation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employee_system_id")
    private UUID systemId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, name = "employee_id")
    private String employeeId;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(unique = true, nullable = false, length = 254)
    private String email;

    @Column(unique = true, nullable = false, length = 10)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Department department;

    @Enumerated(EnumType.STRING)
    private Designation designation;

    @Builder.Default
    @Column(nullable = false)
    private Boolean enabled = Boolean.TRUE;

    @Builder.Default
    @Column(nullable = false)
    private Boolean employed = Boolean.TRUE;

    @Builder.Default
    @Column(nullable = false)
    private Integer failedLoginAttempts = 0;

    @Builder.Default
    @Column(nullable = false)
    private Boolean accountLocked = Boolean.FALSE;

    @Builder.Default
    @Column(nullable = false)
    private Boolean accountExpired = Boolean.FALSE;

    @Builder.Default
    @Column(nullable = false)
    private Boolean credentialsExpired = Boolean.TRUE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime lastLoginAt;

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "employee_roles", joinColumns = @JoinColumn(name = "employee_system_id", referencedColumnName = "employee_system_id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();
}