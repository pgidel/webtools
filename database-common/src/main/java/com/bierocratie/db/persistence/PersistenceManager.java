package com.bierocratie.db.persistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 24/11/15
 * Time: 19:52
 * To change this template use File | Settings | File Templates.
 */
public class PersistenceManager {

    // FIXME LOG
    // private static final Logger LOG = LoggerFactory.getLogger(PersistenceManager.class);

	private static final String persistenceUnitName = "dashboard";
	
    private static final PersistenceManager singleton = new PersistenceManager();

    protected EntityManagerFactory emf;

    public static PersistenceManager getInstance() {
        return singleton;
    }

    private PersistenceManager() {
    }

    public EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            createEntityManagerFactory();
        }
        return emf;
    }

    public void closeEntityManagerFactory() {
        if (emf != null) {
            emf.close();
            emf = null;
            // TODO LOG
            // System.out.println("n*** Persistence finished at " + new java.util.Date());
        }
    }

    protected void createEntityManagerFactory() {
        // TODO Inject
        this.emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        // TODO LOG
        // System.out.println("n*** Persistence started at " + new java.util.Date());
    }

}
