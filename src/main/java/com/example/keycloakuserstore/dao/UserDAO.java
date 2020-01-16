package com.example.keycloakuserstore.dao;

import com.example.keycloakuserstore.models.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class UserDAO {

    private EntityManager entityManager;

    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createNamedQuery("searchForUser", User.class);
        query.setParameter("search", "%");
        return query.getResultList();
    }

    public User createUser(User user) {
        entityManager.persist(user);
        return user;
    }

}
