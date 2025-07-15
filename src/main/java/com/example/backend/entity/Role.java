package com.example.backend.entity;

import com.example.backend.enums.RoleName;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private RoleName roleName;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
