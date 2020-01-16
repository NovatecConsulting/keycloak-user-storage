package com.example.keycloakuserstore.dao;

import com.example.keycloakuserstore.models.User;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class UserDAO {

    private EntityManager entityManager;
    Logger logger = Logger.getLogger(UserDAO.class.getName());

    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createNamedQuery("searchForUser", User.class);
        query.setParameter("search", "%");
        return query.getResultList();
    }

    public List<User> findAll(int start, int max) {
        TypedQuery<User> query = entityManager.createNamedQuery("searchForUser", User.class);
        query.setFirstResult(start);
        query.setMaxResults(max);
        query.setParameter("search", "%");
        List<User> users =  query.getResultList();
        System.err.println(users);
        return users;
    }

    public User getUserByUsername(String username) {
        logger.info("getUserByUsername(username: " + username + ")");
        TypedQuery<User> query = entityManager.createNamedQuery("getUserByUsername", User.class);
        query.setParameter("username", username);
        return query.getResultList().get(0);
    }

    public List<User> searchForUserByUsernameOrEmail(String searchString) {
        logger.info("searchForUserByUsernameOrEmail(searchString: " + searchString + ")");
        TypedQuery<User> query = entityManager.createNamedQuery("searchForUser", User.class);
        query.setParameter("search", searchString);
        return query.getResultList();
    }

    public User createUser(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();
        return user;
    }

    public void close() {
        this.entityManager.close();
    }

}
