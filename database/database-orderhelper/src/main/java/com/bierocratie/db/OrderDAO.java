package com.bierocratie.db;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 13/10/14
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */
//FIXME
//@UIScoped
public class OrderDAO {

    private String persistenceUnitName;

    public OrderDAO(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

}
