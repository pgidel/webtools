package com.bierocratie.db.accounting;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 01/11/14
 * Time: 12:06
 * To change this template use File | Settings | File Templates.
 */
//FIXME
//@UIScoped
public class StockValueDAO {

    private static final String SELECT_STOCK = "SELECT s.amount FROM StockValue s WHERE s.year.year=:year";

    private String persistenceUnitName = "dashboard";

    @PostConstruct
    public void init() {
    }

    public StockValueDAO(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public BigDecimal getStockHTByYear(String year) throws SQLException {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_STOCK);
        query.setParameter("year", year);
        if (query.getResultList().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal((Double) query.getSingleResult());
    }

}
