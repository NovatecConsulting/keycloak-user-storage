package com.example.DemoMySQLKeycloakAPI.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "UserEntity")
@Data
@Accessors(chain = true)
public class User {
    @Id
    @GeneratedValue
    @Column(length = 128/8)
    private UUID id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phone;

    public User updateUser(User updated) {
        return this.setEmail(updated.getEmail())
                .setPassword(updated.getPassword())
                .setPhone(updated.getPhone())
                .setUsername(updated.getUsername());
    }
}