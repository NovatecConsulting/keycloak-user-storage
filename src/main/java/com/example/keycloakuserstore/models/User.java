package com.example.keycloakuserstore.models;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name="getUserByUsername", query="select u from User u where u.username = :username"),
        @NamedQuery(name="getUserByEmail", query="select u from User u where u.email = :email"),
        @NamedQuery(name="getUserCount", query="select count(u) from User u"),
        @NamedQuery(name="getAllUsers", query="select u from User u"),
        @NamedQuery(name="searchForUser", query="select u from User u where " +
                "( lower(u.username) like :search or u.email like :search ) order by u.username"),
})
@Entity
@Table(name = "UserEntity")
@Data
@Accessors(chain = true)
public class User {
    @Id
    @GeneratedValue
    private String id;
    private String username;
    private String email;
    private String password;
    private String phone;
}
