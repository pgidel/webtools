package com.bierocratie.db.accounting;

import com.bierocratie.model.accounting.BudgetYear;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 01/11/14
 * Time: 12:06
 * To change this template use File | Settings | File Templates.
 */
//FIXME
//@UIScoped
public class IncomeDAO {

    private static final String SELECT_SUM_INCOMES_BY_MONTH = "SELECT i.month, SUM(i.amount*(1+i.tva.rate)) FROM Income i GROUP BY i.month";
    private static final String SELECT_SUM_INCOMES_HT_BY_MONTH = "SELECT i.month, SUM(i.amount) FROM Income i GROUP BY i.month";

    private static final String SELECT_SUM_INCOMES_BY_YEAR = "select sum(i.amount*(1+i.tva.rate)) from Income i inner join BudgetYear b on b.firstMonth<=i.month and b.lastMonth>=i.month and b.year=:year";
    private static final String SELECT_SUM_CURRENT_INCOMES_BY_YEAR = "select sum(i.amount*(1+i.tva.rate)) from Income i inner join BudgetYear b on b.firstMonth<=i.month and :currentMonth>=i.month and b.year=:year";
    private static final String SELECT_SUM_INCOMES_HT_BY_YEAR = "select sum(i.amount) from Income i inner join BudgetYear b on b.firstMonth<=i.month and b.lastMonth>=i.month and b.year=:year";
    private static final String SELECT_SUM_CURRENT_INCOMES_HT_BY_YEAR = "select sum(i.amount) from Income i inner join BudgetYear b on b.firstMonth<=i.month and :currentMonth>=i.month and b.year=:year";

    private String persistenceUnitName = "dashboard";

    public IncomeDAO(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public Map<String, BigInteger> getSumIncomesByMonth() throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Map<String, BigInteger> results = new HashMap<>();
        Query query = entityManager.createQuery(SELECT_SUM_INCOMES_BY_MONTH);
        List<Object[]> list = query.getResultList();
        for (Object[] r : list) {
            results.put((String) r[0], ((BigDecimal) r[1]).toBigInteger());
        }

        return results;
    }

    public BigDecimal getSumCurrentIncomesByYear(String year) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_SUM_CURRENT_INCOMES_BY_YEAR);
        query.setParameter("year", year);
        query.setParameter("currentMonth", BudgetYear.getCurrentMonth());
        if (query.getSingleResult() == null) {
            return BigDecimal.ZERO;
        }
        return (BigDecimal) query.getSingleResult();
    }

    public BigDecimal getSumCurrentIncomesHTByYear(String year) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_SUM_CURRENT_INCOMES_HT_BY_YEAR);
        query.setParameter("year", year);
        query.setParameter("currentMonth", BudgetYear.getCurrentMonth());
        if (query.getSingleResult() == null) {
            return BigDecimal.ZERO;
        }
        return (BigDecimal) query.getSingleResult();
    }

    public Map<String, BigInteger> getSumIncomesHTByMonth() throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Map<String, BigInteger> results = new HashMap<>();
        Query query = entityManager.createQuery(SELECT_SUM_INCOMES_HT_BY_MONTH);
        List<Object[]> list = query.getResultList();
        for (Object[] r : list) {
            results.put((String) r[0], ((BigDecimal) r[1]).toBigInteger());
        }

        return results;
    }

    public BigDecimal getSumIncomesByYear(String year) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_SUM_INCOMES_BY_YEAR);
        query.setParameter("year", year);
        if (query.getSingleResult() == null) {
            return BigDecimal.ZERO;
        }
        return (BigDecimal) query.getSingleResult();
    }

    public BigDecimal getSumIncomesHTByYear(String year) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_SUM_INCOMES_HT_BY_YEAR);
        query.setParameter("year", year);
        if (query.getSingleResult() == null) {
            return BigDecimal.ZERO;
        }
        return (BigDecimal) query.getSingleResult();
    }

}
