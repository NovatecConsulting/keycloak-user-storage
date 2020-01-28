package com.example.DemoMySQLKeycloakAPI.repositories;

import com.example.DemoMySQLKeycloakAPI.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(value = "SELECT * FROM user_entity WHERE COALESCE(username, '') LIKE %:username% AND COALESCE(email, '') LIKE %:email% ORDER BY username DESC", nativeQuery = true)
    List<User> findByUsernameContainingAndEmailContaining(String username, String email);
    @Query(value = "SELECT * FROM user_entity WHERE COALESCE(username, '') LIKE %:username% AND COALESCE(email, '') LIKE %:email% ORDER BY username LIMIT :min, :max", nativeQuery = true)
    List<User> findByUsernameContainingAndEmailContaining(String username, String email, int min, int max);
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsUserByUsernameOrEmail(String username, String email);
}
