package com.bierocratie.db.security;

import com.bierocratie.model.security.Account;
import com.bierocratie.model.security.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 10/10/14
 * Time: 19:26
 * To change this template use File | Settings | File Templates.
 */
//FIXME
//@UIScoped
public class AccountDAO {

    private static final String SELECT_PASSWORD_BY_LOGIN_QUERY = "SELECT a.password FROM Account a WHERE a.login=:login";
    private static final String SELECT_ROLE_BY_LOGIN_QUERY = "SELECT a.role FROM Account a WHERE a.login=:login";
    private static final String SELECT_BY_LOGIN_AND_EMAIL_QUERY = "FROM Account a WHERE a.login=:login AND a.email=:email";
    private static final String SELECT_BY_LOGIN_AND_PASSWORD_QUERY = "FROM Account a WHERE a.login=:login AND a.password=:password";

    private String persistenceUnitName = "dashboard";

    public AccountDAO(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public byte[] getPasswordForUser(String username) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_PASSWORD_BY_LOGIN_QUERY);
        query.setParameter("login", username);
        List<byte[]> results = query.getResultList();

        if (results.isEmpty()) {
            throw new SQLException("No user row found for user [" + username + "].");
        }
        if (results.size() > 1) {
            throw new SQLException("More than one user row found for user [" + username + "]. Usernames must be unique.");
        }

        return results.get(0);
    }

    public String getRoleNamesForUser(String username) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_ROLE_BY_LOGIN_QUERY);
        query.setParameter("login", username);
        List<Role> results = query.getResultList();

        if (results.isEmpty()) {
            throw new SQLException("No user row found for user [" + username + "].");
        }
        return results.get(0).name();
    }

    public Account getAccountByLoginAndEmail(String username, String email) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_BY_LOGIN_AND_EMAIL_QUERY);
        query.setParameter("login", username);
        query.setParameter("email", email);

        List<Account> results = query.getResultList();
        if (results.isEmpty()) {
            throw new SQLException("No user row found for user [" + username + "].");
        }
        return results.get(0);
    }

    public Account getAccountByLoginAndPassword(String username, String password) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_BY_LOGIN_AND_PASSWORD_QUERY);
        query.setParameter("login", username);
        query.setParameter("password", password);

        List<Account> results = query.getResultList();
        if (results.isEmpty()) {
            throw new SQLException("No user row found for user [" + username + "].");
        }

        return results.get(0);
    }

}
