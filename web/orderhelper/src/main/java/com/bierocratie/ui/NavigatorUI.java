package com.bierocratie.ui;

import com.bierocratie.model.catalog.Capacity;
import com.bierocratie.model.security.Account;
import com.bierocratie.security.AdminGenerator;
import com.bierocratie.ui.view.CustomerView;
import com.bierocratie.ui.view.ErrorView;
import com.bierocratie.ui.view.InProgressView;
import com.bierocratie.ui.view.OrderView;
import com.bierocratie.ui.view.catalog.BeerView;
import com.bierocratie.ui.view.catalog.CapacityView;
import com.bierocratie.ui.view.catalog.SupplierView;
import com.bierocratie.ui.view.security.AbstractLoginView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.mail.MessagingException;
import javax.servlet.annotation.WebServlet;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 28/04/14
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 */
// FIXME @CDIUI("orderhelper")
@PreserveOnRefresh
public class NavigatorUI extends UI implements ViewChangeListener {

    public static final String IN_PROGRESS_VIEW = "en_cours";
    public static final String ORDER_VIEW = "commandes";
    public static final String CUSTOMER_VIEW = "caves";
    public static final String SUPPLIER_VIEW = "brasseries";
    public static final String BEER_VIEW = "bieres";
    public static final String CAPACITY_VIEW = "formats";
    public static final String LOGIN_VIEW = "";

    private static final Logger LOG = LoggerFactory.getLogger(NavigatorUI.class);

    // FIXME Initialisation pour tests => Supprimer
    static {
        final JPAContainer<Account> accountEntities = JPAContainerFactory.make(Account.class, "orderhelper");
        if (accountEntities.getItemIds().isEmpty()) {
            try {
                accountEntities.addEntity(AdminGenerator.generateAdminAccount());
            } catch (MessagingException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
                LOG.error("Erreur à l'initialisation de l'application", e);
                System.exit(0);
            }
        }
        final JPAContainer<Capacity> capacityEntities = JPAContainerFactory.make(Capacity.class, "orderhelper");
        if (capacityEntities.getItemIds().isEmpty()) {
            capacityEntities.addEntity(new Capacity("33cl", 0.33, 0.5));
            capacityEntities.addEntity(new Capacity("50cl", 0.50, 0.6));
            capacityEntities.addEntity(new Capacity("75cl", 0.75, 0.8));
        }
    }

    @Override
    protected void init(VaadinRequest request) {
        Navigator navigator = new Navigator(this, this);
        navigator.addViewChangeListener(this);

        navigator.addView(LOGIN_VIEW, AbstractLoginView.class);
        if (SecurityUtils.getSubject().isAuthenticated()) {
            // Ajoute les vues après fermeture/ouverture de la fenêtre
            navigator.addView(IN_PROGRESS_VIEW, InProgressView.class);
            navigator.addView(ORDER_VIEW, OrderView.class);
            navigator.addView(CUSTOMER_VIEW, CustomerView.class);
            navigator.addView(SUPPLIER_VIEW, SupplierView.class);
            navigator.addView(BEER_VIEW, BeerView.class);
            navigator.addView(CAPACITY_VIEW, CapacityView.class);
        }
        navigator.setErrorView(ErrorView.class);
    }

    @Override
    public boolean beforeViewChange(ViewChangeListener.ViewChangeEvent event) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated() && event.getViewName().equals(LOGIN_VIEW)) {
            event.getNavigator().addView(IN_PROGRESS_VIEW, InProgressView.class);
            event.getNavigator().addView(ORDER_VIEW, OrderView.class);
            event.getNavigator().addView(CUSTOMER_VIEW, CustomerView.class);
            event.getNavigator().addView(SUPPLIER_VIEW, SupplierView.class);
            event.getNavigator().addView(BEER_VIEW, BeerView.class);
            event.getNavigator().addView(CAPACITY_VIEW, CapacityView.class);

            event.getNavigator().navigateTo(BEER_VIEW);
            return false;
        }

        if (!currentUser.isAuthenticated() && !event.getViewName().equals(LOGIN_VIEW)) {
            event.getNavigator().navigateTo(LOGIN_VIEW);
            return false;
        }

        return true;
    }

    @Override
    public void afterViewChange(ViewChangeEvent event) {
    }

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = NavigatorUI.class)
    public static class Servlet extends VaadinServlet {
    }

}
