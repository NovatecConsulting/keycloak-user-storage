package com.example.keycloakuserstore.dao;

import com.example.keycloakuserstore.models.User;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class UserDAO {

    private EntityManager entityManager;
    Logger logger = Logger.getLogger(UserDAO.class.getName());

    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<User> findAll() {
        return findAll(null, null);
    }

    public List<User> findAll(int start, int max) {
        return findAll((Integer)start, (Integer)max);
    }

    private List<User> findAll(Integer start, Integer max) {
        TypedQuery<User> query = entityManager.createNamedQuery("searchForUser", User.class);
        if(start != null) {
            query.setFirstResult(start);
        }
        if(max != null) {
            query.setMaxResults(max);
        }
        query.setParameter("search", "%");
        List<User> users =  query.getResultList();
        return users;
    }

    public Optional<User> getUserByUsername(String username) {
        logger.info("getUserByUsername(username: " + username + ")");
        TypedQuery<User> query = entityManager.createNamedQuery("getUserByUsername", User.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }

    public Optional<User> getUserByEmail(String email) {
        logger.info("getUserByEmail(email: " + email + ")");
        TypedQuery<User> query = entityManager.createNamedQuery("getUserByEmail", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    public List<User> searchForUserByUsernameOrEmail(String searchString) {
        logger.info("searchForUserByUsernameOrEmail(searchString: " + searchString + ")");
        return searchForUserByUsernameOrEmail(searchString, null, null);
    }

    public List<User> searchForUserByUsernameOrEmail(String searchString, int start, int max) {
        logger.info("searchForUserByUsernameOrEmail(searchString: " + searchString + ", start: "+start+", max: "+max+")");
        return searchForUserByUsernameOrEmail(searchString, (Integer)start, (Integer)max);
    }

    private List<User> searchForUserByUsernameOrEmail(String searchString, Integer start, Integer max) {
        logger.info("searchForUserByUsernameOrEmail(searchString: " + searchString + ", start: "+start+", max: "+max+")");
        TypedQuery<User> query = entityManager.createNamedQuery("searchForUser", User.class);
        query.setParameter("search", "%" + searchString + "%");
        if(start != null) {
            query.setFirstResult(start);
        }
        if(max != null) {
            query.setMaxResults(max);
        }
        return query.getResultList();
    }

    public User getUserById(String id) {
        logger.info("getUserById(id: " + id + ")");
        return entityManager.find(User.class, UUID.fromString(id));
    }

    public User createUser(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();
        return user;
    }

    public void deleteUser(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.remove(user);
        transaction.commit();
    }

    public void close() {
        this.entityManager.close();
    }

    public User updateUser(User userEntity) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(userEntity);
        transaction.commit();
        return userEntity;
    }

    public int size() {
        return entityManager.createNamedQuery("getUserCount", Integer.class).getSingleResult();
    }
}
