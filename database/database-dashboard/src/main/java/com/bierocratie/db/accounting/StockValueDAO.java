package com.bierocratie.db.accounting;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import com.bierocratie.db.persistence.PersistenceManager;

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

    private static final EntityManagerFactory entityManagerFactory = PersistenceManager.getInstance().getEntityManagerFactory();

    public BigDecimal getStockHTByYear(String year) throws SQLException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createQuery(SELECT_STOCK);
        query.setParameter("year", year);
        if (query.getResultList().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal((Double) query.getSingleResult());
    }

}
