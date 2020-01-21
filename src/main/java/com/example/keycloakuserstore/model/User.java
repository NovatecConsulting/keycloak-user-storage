package com.example.keycloakuserstore.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class User {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private String phone;
}
