package com.bierocratie.db.accounting;

import com.bierocratie.model.accounting.BudgetYear;
import com.bierocratie.model.accounting.CategoryAndMonth;
import com.bierocratie.model.accounting.Invoice;
import com.bierocratie.model.accounting.Tva;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 10/10/14
 * Time: 19:26
 * To change this template use File | Settings | File Templates.
 */
//FIXME
//@UIScoped
public class InvoiceDAO {

    private static final String SELECT_MONTHS_BY_YEAR_FOR_CASH = "SELECT DISTINCT(i.month) FROM Invoice i INNER JOIN BudgetYear b " +
            "ON i.month>=b.firstMonth AND i.month<=b.lastMonth WHERE b.year=:year ORDER BY i.month";
    private static final String SELECT_MONTHS_BY_YEAR_FOR_OPERATING = "SELECT DISTINCT(SUBSTRING(i.d,0,5)) FROM Invoice i INNER JOIN BudgetYear b " +
            "ON SUBSTRING(i.d,0,5)>=b.firstMonth AND SUBSTRING(i.d,0,5)<=b.lastMonth WHERE b.year=:year ORDER BY SUBSTRING(i.d,0,5)";

    private static final String SELECT_TOTAL_BY_CATEGORY_AND_MONTH = "select new com.bierocratie.model.accounting.CategoryAndMonth(i.category, i.month, sum(i.amount*i.category.percentageDueByTheCompany))" +
            "from Invoice i where i.month=:month group by i.category order by i.category";
    private static final String SELECT_TOTAL_BY_MONTH_WITH_NO_CATEGORY = "select sum(i.amount)" +
            " from Invoice i where i.month=:month and i.category is null";
    private static final String SELECT_CURRENT_TOTAL_BY_CATEGORY_AND_YEAR = "select new com.bierocratie.model.accounting.CategoryAndMonth(i.category, i.month, sum(i.amount*i.category.percentageDueByTheCompany))" +
            "from Invoice i inner join BudgetYear b on i.month>=b.firstMonth and i.month<=:currentMonth where b.year=:year group by i.category order by i.category";
    private static final String SELECT_CURRENT_TOTAL_BY_YEAR_WITH_NO_CATEGORY = "select sum(i.amount)" +
            " from Invoice i inner join BudgetYear b on i.month>=b.firstMonth and i.month<=:currentMonth where b.year=:year and i.category is null";
    private static final String SELECT_CURRENT_TOTAL_HT_BY_CATEGORY_AND_YEAR = "select new com.bierocratie.model.accounting.CategoryAndMonth(i.category, substring(i.d,0,5), sum(i.amount*i.category.percentageDueByTheCompany/(1+i.tva.rate)))" +
            "from Invoice i inner join BudgetYear b on substring(i.d,0,5)>=b.firstMonth and substring(i.d,0,5)<=:currentMonth where b.year=:year group by i.category order by i.category";
    private static final String SELECT_CURRENT_TOTAL_HT_BY_YEAR_WITH_NO_CATEGORY = "select sum(i.amount/(1+i.tva.rate))" +
            " from Invoice i inner join BudgetYear b on substring(i.d,0,5)>=b.firstMonth and substring(i.d,0,5)<=:currentMonth where b.year=:year and i.category is null";
    private static final String SELECT_TOTAL_BY_CATEGORY_AND_YEAR = "select new com.bierocratie.model.accounting.CategoryAndMonth(i.category, i.month, sum(i.amount*i.category.percentageDueByTheCompany))" +
            "from Invoice i inner join BudgetYear b on i.month>=b.firstMonth and i.month<=b.lastMonth where b.year=:year group by i.category order by i.category";
    private static final String SELECT_TOTAL_BY_YEAR_WITH_NO_CATEGORY = "select sum(i.amount)" +
            " from Invoice i inner join BudgetYear b on i.month>=b.firstMonth and i.month<=b.lastMonth where b.year=:year and i.category is null";
    private static final String SELECT_TOTAL_HT_BY_CATEGORY_AND_YEAR = "select new com.bierocratie.model.accounting.CategoryAndMonth(i.category, substring(i.d,0,5), sum(i.amount*i.category.percentageDueByTheCompany/(1+i.tva.rate)))" +
            "from Invoice i inner join BudgetYear b on substring(i.d,0,5)>=b.firstMonth and substring(i.d,0,5)<=b.lastMonth where b.year=:year group by i.category order by i.category";
    private static final String SELECT_TOTAL_HT_BY_YEAR_WITH_NO_CATEGORY = "select sum(i.amount/(1+i.tva.rate))" +
            " from Invoice i inner join BudgetYear b on substring(i.d,0,5)>=b.firstMonth and i.month<=b.lastMonth where b.year=:year and i.category is null";
    private static final String SELECT_TOTAL_HT_BY_CATEGORY_AND_DATE = "select new com.bierocratie.model.accounting.CategoryAndMonth(i.category, substring(i.d,0,5), sum(i.amount*i.category.percentageDueByTheCompany/(1+i.tva.rate)))" +
            " from Invoice i where substring(i.d,0,5)=:month group by i.category order by i.category";
    private static final String SELECT_TOTAL_HT_BY_DATE_WITH_NO_CATEGORY = "select sum(i.amount/(1+i.tva.rate))" +
            " from Invoice i where substring(i.d,0,5)=:month and i.category is null";

    private static final String SELECT_SUM_INVOICES_BY_MONTH = "SELECT i.month, SUM(i.amount*i.category.percentageDueByTheCompany) FROM Invoice i GROUP BY i.month";
    private static final String SELECT_SUM_INVOICES_BY_MONTH_WITH_NO_CATEGORY = "SELECT i.month, SUM(i.amount) FROM Invoice i WHERE i.category is null GROUP BY i.month";
    private static final String SELECT_SUM_INVOICES_HT_BY_DATE = "SELECT substring(i.d,0,5), SUM(i.amount*i.category.percentageDueByTheCompany/(1+i.tva.rate)) FROM Invoice i GROUP BY substring(i.d,0,5)";
    private static final String SELECT_SUM_INVOICES_HT_BY_DATE_WITH_NO_CATEGORY = "SELECT substring(i.d,0,5), SUM(i.amount/(1+i.tva.rate)) FROM Invoice i WHERE i.category is null GROUP BY substring(i.d,0,5)";

    private static final String UPDATE_TVA_BEFORE_DATE = "UPDATE Invoice i SET i.tva=:tva WHERE i.tva IS NULL AND i.date < :date";
    private static final String UPDATE_TVA_AFTER_DATE = "UPDATE Invoice i SET i.tva=:tva WHERE i.tva IS NULL AND i.date >= :date";

    private static final String SELECT_SUM_STOCK_INVOICES_BY_YEAR = "select sum(i.amount) from Invoice i inner join BudgetYear b on b.firstMonth<=i.month and b.lastMonth>=i.month AND b.year=:year WHERE i.category.name='Achat stock'";
    private static final String SELECT_SUM_STOCK_INVOICES_HT_BY_YEAR = "select sum(i.amount/(1+i.tva.rate)) from Invoice i inner join BudgetYear b on b.firstMonth<=substring(i.d,0,5) and b.lastMonth>=substring(i.d,0,5) AND b.year=:year WHERE i.category.name='Achat stock'";

    private static final String SELECT_SUM_CURRENT_INVOICES_BY_YEAR = "select sum(i.amount) from Invoice i inner join BudgetYear b on b.firstMonth<=i.month and :currentMonth>=i.month AND b.year=:year";
    private static final String SELECT_SUM_CURRENT_INVOICES_HT_BY_YEAR = "select sum(i.amount/(1+i.tva.rate)) from Invoice i inner join BudgetYear b on :currentMonth<=substring(i.d,0,5) and b.lastMonth>=substring(i.d,0,5) AND b.year=:year";

    private String persistenceUnitName = "dashboard";

    public InvoiceDAO(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public Invoice find(String date, String supplier, BigDecimal amount) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Invoice> criteriaQuery = criteriaBuilder.createQuery(Invoice.class);

        Root<Invoice> from = criteriaQuery.from(Invoice.class);
        Predicate conditionDate = criteriaBuilder.equal(from.get("d"), date);
        Predicate conditionSupplier = criteriaBuilder.equal(from.get("supplier"), supplier);
        Predicate conditionAmount = criteriaBuilder.equal(from.get("amount"), amount);
        criteriaQuery.where(criteriaBuilder.and(conditionDate, criteriaBuilder.and(conditionSupplier, conditionAmount)));

        try {
            Query query = entityManager.createQuery(criteriaQuery);
            return (Invoice) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<String> getMonthsByYearForCash(String year) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_MONTHS_BY_YEAR_FOR_CASH);
        query.setParameter("year", year);
        List<String> results = query.getResultList();

        return results;
    }

    public List<String> getMonthsByYearForOperating(String year) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_MONTHS_BY_YEAR_FOR_OPERATING);
        query.setParameter("year", year);
        List<String> results = query.getResultList();

        return results;
    }

    public List<CategoryAndMonth> getAmountsByMonthForCash(String month) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_TOTAL_BY_CATEGORY_AND_MONTH);
        query.setParameter("month", month);
        List<CategoryAndMonth> results = query.getResultList();

        query = entityManager.createQuery(SELECT_TOTAL_BY_MONTH_WITH_NO_CATEGORY);
        query.setParameter("month", month);
        if (query.getSingleResult() != null) {
            results.add(new CategoryAndMonth(null, month, (BigDecimal) query.getSingleResult()));
        }

        return results;
    }

    public List<CategoryAndMonth> getAmountsByYearForCash(String year) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_TOTAL_BY_CATEGORY_AND_YEAR);
        query.setParameter("year", year);
        List<CategoryAndMonth> results = query.getResultList();

        query = entityManager.createQuery(SELECT_TOTAL_BY_YEAR_WITH_NO_CATEGORY);
        query.setParameter("year", year);
        if (query.getSingleResult() != null) {
            results.add(new CategoryAndMonth(null, year, (BigDecimal) query.getSingleResult()));
        }

        return results;
    }

    public List<CategoryAndMonth> getAmountsByYearForOperating(String year) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_TOTAL_HT_BY_CATEGORY_AND_YEAR);
        query.setParameter("year", year);
        List<CategoryAndMonth> results = query.getResultList();

        query = entityManager.createQuery(SELECT_TOTAL_HT_BY_YEAR_WITH_NO_CATEGORY);
        query.setParameter("year", year);
        if (query.getSingleResult() != null) {
            results.add(new CategoryAndMonth(null, year, (BigDecimal) query.getSingleResult()));
        }

        return results;
    }

    public List<CategoryAndMonth> getCurrentAmountsByYearForCash(String year) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_CURRENT_TOTAL_BY_CATEGORY_AND_YEAR);
        query.setParameter("currentMonth", BudgetYear.getCurrentMonth());
        query.setParameter("year", year);
        List<CategoryAndMonth> results = query.getResultList();

        query = entityManager.createQuery(SELECT_CURRENT_TOTAL_BY_YEAR_WITH_NO_CATEGORY);
        query.setParameter("currentMonth", BudgetYear.getCurrentMonth());
        query.setParameter("year", year);
        if (query.getSingleResult() != null) {
            results.add(new CategoryAndMonth(null, year, (BigDecimal) query.getSingleResult()));
        }

        return results;
    }

    public List<CategoryAndMonth> getCurrentAmountsByYearForOperating(String year) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_CURRENT_TOTAL_HT_BY_CATEGORY_AND_YEAR);
        query.setParameter("currentMonth", BudgetYear.getCurrentMonth());
        query.setParameter("year", year);
        List<CategoryAndMonth> results = query.getResultList();

        query = entityManager.createQuery(SELECT_CURRENT_TOTAL_HT_BY_YEAR_WITH_NO_CATEGORY);
        query.setParameter("currentMonth", BudgetYear.getCurrentMonth());
        query.setParameter("year", year);
        if (query.getSingleResult() != null) {
            results.add(new CategoryAndMonth(null, year, (BigDecimal) query.getSingleResult()));
        }

        return results;
    }

    public List<CategoryAndMonth> getAmountsHTByMonthForOperating(String month) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_TOTAL_HT_BY_CATEGORY_AND_DATE);
        query.setParameter("month", month);
        List<CategoryAndMonth> results = query.getResultList();

        query = entityManager.createQuery(SELECT_TOTAL_HT_BY_DATE_WITH_NO_CATEGORY);
        query.setParameter("month", month);
        if (query.getSingleResult() != null) {
            results.add(new CategoryAndMonth(null, month, (BigDecimal) query.getSingleResult()));
        }

        return results;
    }

    public Map<String, BigInteger> getSumInvoiceByMonthForCash() throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Map<String, BigInteger> results = new HashMap<>();
        Query query = entityManager.createQuery(SELECT_SUM_INVOICES_BY_MONTH);
        List<Object[]> list = query.getResultList();
        for (Object[] r : list) {
            results.put((String) r[0], new BigDecimal((double) r[1]).toBigInteger());
        }

        query = entityManager.createQuery(SELECT_SUM_INVOICES_BY_MONTH_WITH_NO_CATEGORY);
        list = query.getResultList();
        for (Object[] r : list) {
            String month = (String) r[0];
            BigInteger sum = ((BigDecimal) r[1]).toBigInteger();
            if (results.get(month) != null) {
                results.put(month, results.get(month).add(sum));
            } else {
                results.put(month, sum);
            }
        }

        return results;
    }

    public Map<String, BigInteger> getSumInvoiceByMonthForOperating() throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Map<String, BigInteger> results = new HashMap<>();
        Query query = entityManager.createQuery(SELECT_SUM_INVOICES_HT_BY_DATE);
        List<Object[]> list = query.getResultList();
        for (Object[] r : list) {
            results.put((String) r[0], new BigDecimal((double) r[1]).toBigInteger());
        }

        query = entityManager.createQuery(SELECT_SUM_INVOICES_HT_BY_DATE_WITH_NO_CATEGORY);
        list = query.getResultList();
        for (Object[] r : list) {
            String month = (String) r[0];
            BigInteger sum = ((BigDecimal) r[1]).toBigInteger();
            if (results.get(month) != null) {
                results.put(month, results.get(month).add(sum));
            } else {
                results.put(month, sum);
            }
        }

        return results;
    }

    public void updateTVAByDefault(Tva tva196, Tva tva200, Date date) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Query query = entityManager.createQuery(UPDATE_TVA_BEFORE_DATE);
        query.setParameter("tva", tva196);
        query.setParameter("date", date);
        query.executeUpdate();

        query = entityManager.createQuery(UPDATE_TVA_AFTER_DATE);
        query.setParameter("tva", tva200);
        query.setParameter("date", date);
        query.executeUpdate();
        tx.commit();
    }

    public BigDecimal getSumStockInvoicesHTByYear(String year) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_SUM_STOCK_INVOICES_HT_BY_YEAR);
        query.setParameter("year", year);
        if (query.getSingleResult() == null) {
            return BigDecimal.ZERO;
        }
        return (BigDecimal) query.getSingleResult();
    }

    public BigDecimal getSumStockInvoicesByYear(String year) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_SUM_STOCK_INVOICES_BY_YEAR);
        query.setParameter("year", year);
        if (query.getSingleResult() == null) {
            return BigDecimal.ZERO;
        }
        return (BigDecimal) query.getSingleResult();
    }

    public BigDecimal getSumCurrentInvoicesHTByYear(String year) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_SUM_CURRENT_INVOICES_HT_BY_YEAR);
        query.setParameter("year", year);
        query.setParameter("currentMonth", BudgetYear.getCurrentMonth());
        if (query.getSingleResult() == null) {
            return BigDecimal.ZERO;
        }
        return (BigDecimal) query.getSingleResult();
    }

    public BigDecimal getSumCurrentInvoicesByYear(String year) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_SUM_CURRENT_INVOICES_BY_YEAR);
        query.setParameter("year", year);
        query.setParameter("currentMonth", BudgetYear.getCurrentMonth());
        if (query.getSingleResult() == null) {
            return BigDecimal.ZERO;
        }
        return (BigDecimal) query.getSingleResult();
    }

}
