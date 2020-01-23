package com.example.keycloakuserstore.services;

import com.example.keycloakuserstore.errors.UserStorageException;
import com.example.keycloakuserstore.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Optional<User> getUserById(UUID id) throws UserStorageException;
    List<User> getAllUsers() throws UserStorageException;

    List<User> getAllUsers(int min, int max) throws UserStorageException;

    Optional<User> updateUser(User updatedUser) throws UserStorageException;
    Optional<User> createUser(User user) throws UserStorageException;
    List<User> findUsersByUsernameOrEmail(String username, String email) throws UserStorageException;

    List<User> findUsersByUsernameOrEmail(String username, String email, int min, int max) throws UserStorageException;

    Optional<User> findUserByUsername(String username) throws UserStorageException;
    Optional<User> findUserByEmail(String email) throws UserStorageException;
    void deleteUser(String id) throws UserStorageException;
}
