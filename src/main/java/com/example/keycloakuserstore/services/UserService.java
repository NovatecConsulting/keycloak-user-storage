package com.example.keycloakuserstore.services;

import com.example.keycloakuserstore.errors.UserStorageException;
import com.example.keycloakuserstore.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Optional<User> getUserById(UUID id) throws UserStorageException;
    List<User> getAllUsers() throws UserStorageException;
    Optional<User> updateUser(User updatedUser) throws UserStorageException;
    Optional<User> createUser(User user) throws UserStorageException;
    List<User> findUserByUsernameOrEmail(String username, String email) throws UserStorageException;
    List<User> findUserByUsername(String username) throws UserStorageException;
    List<User> findUserByEmail(String email) throws UserStorageException;

}
