package com.bierocratie.ui;

import com.bierocratie.db.accounting.InvoiceDAO;
import com.bierocratie.db.persistence.PersistenceManager;
import com.bierocratie.model.accounting.Category;
import com.bierocratie.model.accounting.Tva;
import com.bierocratie.model.catalog.Capacity;
import com.bierocratie.model.catalog.SupplierType;
import com.bierocratie.model.diffusion.Medium;
import com.bierocratie.ui.view.ErrorView;
import com.bierocratie.ui.view.accounting.*;
import com.bierocratie.ui.view.catalog.*;
import com.bierocratie.ui.view.chat.ChatView;
import com.bierocratie.ui.view.dashboard.DashboardView;
import com.bierocratie.ui.view.diffusion.DiffusionView;
import com.bierocratie.ui.view.diffusion.MediumView;
import com.porotype.tinycon.Tinycon;
import com.porotype.webnotifications.WebNotification;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import javax.servlet.annotation.WebServlet;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 28/04/14
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 */
// FIXME
//@CDIUI("dashboard")
@Push
@PreserveOnRefresh
@Widgetset("com.bierocratie.ui.view.chat.ChatWidgetSet")
public class NavigatorUI extends UI {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5250690888792419255L;
	public static final String DASHBOARD_VIEW = "";
    public static final String BEER_VIEW = "biere";
    public static final String CASK_VIEW = "fut";
    public static final String SUPPLIER_VIEW = "fournisseurs";
    public static final String SUPPLIER_TYPE_VIEW = "typefournisseur";
    public static final String COUNTRY_VIEW = "pays";
    public static final String CAPACITY_VIEW = "capacite";
    public static final String DIFFUSION_VIEW = "diffusion";
    public static final String MEDIUM_VIEW = "media";
    //public static final String ORDER_VIEW = "commandes";
    //public static final String HOPPY_HOURS_VIEW = "hoppyhours";
    public static final String INVOICE_VIEW = "factures";
    public static final String INCOME_VIEW = "ca";
    public static final String CATEGORY_VIEW = "categories";
    public static final String CASH_PLAN_VIEW = "tresorerie";
    public static final String OPERATING_STATEMENT_VIEW = "exploitation";
    public static final String BUDGET_YEAR_VIEW = "exercice";
    public static final String TVA_VIEW = "tva";
    public static final String STOCK_VALUE_VIEW = "stock";
    public static final String CHAT_VIEW = "chat";

    private static final Logger LOG = LoggerFactory.getLogger(NavigatorUI.class);

    static {
        final JPAContainer<Medium> mediumEntities = JPAContainerFactory.make(Medium.class, "dashboard");
        if (mediumEntities.getItemIds().isEmpty()) {
            mediumEntities.addEntity(new Medium("Facebook"));
            mediumEntities.addEntity(new Medium("Twitter"));
            mediumEntities.addEntity(new Medium("Mailing List"));
            mediumEntities.addEntity(new Medium("Brassam"));
            mediumEntities.addEntity(new Medium("Bière/Fromages"));
            mediumEntities.addEntity(new Medium("Vitrine"));
        }

        final JPAContainer<SupplierType> supplierTypeEntities = JPAContainerFactory.make(SupplierType.class, "dashboard");
        if (supplierTypeEntities.getItemIds().isEmpty()) {
            supplierTypeEntities.addEntity(new SupplierType("Brasserie"));
            supplierTypeEntities.addEntity(new SupplierType("Grossiste"));
        }

        final JPAContainer<Capacity> capacityEntities = JPAContainerFactory.make(Capacity.class, "dashboard");
        if (capacityEntities.getItemIds().isEmpty()) {
            capacityEntities.addEntity(new Capacity("33cl", 0.33, 0.5));
            capacityEntities.addEntity(new Capacity("50cl", 0.50, 0.6));
            capacityEntities.addEntity(new Capacity("75cl", 0.75, 0.8));
            capacityEntities.addEntity(new Capacity("Fût 20L", 20, 25));
            capacityEntities.addEntity(new Capacity("Fût 30L", 30, 35));
        }

        final JPAContainer<Tva> tvaEntities = JPAContainerFactory.make(Tva.class, "dashboard");
        if (tvaEntities.getItemIds().isEmpty()) {
            tvaEntities.addEntity(new Tva(new BigDecimal("0.2")));
            tvaEntities.addEntity(new Tva(new BigDecimal("0.1")));
            tvaEntities.addEntity(new Tva(new BigDecimal("0.055")));
            tvaEntities.addEntity(new Tva(new BigDecimal("0.196")));
            tvaEntities.addEntity(new Tva(new BigDecimal("0.07")));
            tvaEntities.addEntity(new Tva(new BigDecimal("0")));
        }

        final JPAContainer<Category> categoryEntities = JPAContainerFactory.make(Category.class, "dashboard");
        if (categoryEntities.getItemIds().isEmpty()) {
            tvaEntities.addContainerFilter(new Compare.Equal("rate", 0.2));
            Tva defaultTva = tvaEntities.getItem(tvaEntities.firstItemId()).getEntity();

            categoryEntities.addEntity(new Category("Achat marchandises", defaultTva));
            categoryEntities.addEntity(new Category("Internet/téléphone", defaultTva));
            categoryEntities.addEntity(new Category("Electricité", defaultTva));
            categoryEntities.addEntity(new Category("Déplacements", defaultTva));
            categoryEntities.addEntity(new Category("Navigo", defaultTva));
            categoryEntities.addEntity(new Category("Dégustations", defaultTva));
            categoryEntities.addEntity(new Category("Travaux", defaultTva));
            categoryEntities.addEntity(new Category("Fournitures", defaultTva));
            categoryEntities.addEntity(new Category("Impôts & Taxes", defaultTva));
            categoryEntities.addEntity(new Category("Loyer", defaultTva));
            categoryEntities.addEntity(new Category("Associations", defaultTva));
            categoryEntities.addEntity(new Category("Honoraires", defaultTva));
            categoryEntities.addEntity(new Category("RSI", defaultTva));
            categoryEntities.addEntity(new Category("Publicité", defaultTva));
            categoryEntities.addEntity(new Category("Frais bancaires", defaultTva));
            categoryEntities.addEntity(new Category("Assurances", defaultTva));

            tvaEntities.removeAllContainerFilters();
        }
    }

    private WebNotification webNotification = new WebNotification(this);

    public WebNotification getWebNotification() {
        return webNotification;
    }

    private Tinycon t = new Tinycon();

    public Tinycon getTinycon() {
        return t;
    }

    @Override
    protected void init(VaadinRequest request) {
        webNotification.requestPermission();

        // TODO Créer les CSS
        Responsive.makeResponsive();

        Navigator navigator = new Navigator(this, this);
        navigator.setErrorView(ErrorView.class);

        navigator.addView(DASHBOARD_VIEW, DashboardView.class);
        navigator.addView(BEER_VIEW, BeerView.class);
        navigator.addView(CASK_VIEW, CaskView.class);
        navigator.addView(SUPPLIER_VIEW, SupplierView.class);
        navigator.addView(SUPPLIER_TYPE_VIEW, SupplierTypeView.class);
        navigator.addView(COUNTRY_VIEW, CountryView.class);
        navigator.addView(CAPACITY_VIEW, CapacityView.class);
        navigator.addView(DIFFUSION_VIEW, DiffusionView.class);
        navigator.addView(MEDIUM_VIEW, MediumView.class);
        //navigator.addView(ORDER_VIEW, OrderView.class);
        //navigator.addView(HOPPY_HOURS_VIEW, HoppyHoursView.class);
        navigator.addView(INCOME_VIEW, IncomeView.class);
        navigator.addView(INVOICE_VIEW, InvoiceByCategoryView.class);
        navigator.addView(CATEGORY_VIEW, CategoryView.class);
        navigator.addView(CASH_PLAN_VIEW, CashPlanView.class);
        navigator.addView(OPERATING_STATEMENT_VIEW, OperatingStatementView.class);
        navigator.addView(BUDGET_YEAR_VIEW, BudgetYearView.class);
        navigator.addView(TVA_VIEW, TvaView.class);
        navigator.addView(STOCK_VALUE_VIEW, StockValueView.class);
        navigator.addView(CHAT_VIEW, ChatView.class);

        getUI().getPage().setTitle("Tableau de bord");
        navigator.navigateTo(DASHBOARD_VIEW);

        t.extend(this);

        UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -615894631556635756L;

			@Override
            public void error(com.vaadin.server.ErrorEvent event) {
                Throwable throwableCause = null;
                for (Throwable t = event.getThrowable(); t != null; t = t.getCause()) {
                    if (t.getCause() == null) {
                        throwableCause = t;
                    }
                }

                if (throwableCause instanceof SQLException) {
                    LOG.error("", throwableCause);
                } else {
                    doDefault(event);
                }
                // FIXME ne pas perdre la session
            }
        });

        try {
            InvoiceDAO invoiceDAO = new InvoiceDAO();
            final JPAContainer<Tva> tvaEntities = JPAContainerFactory.make(Tva.class, "dashboard");

            tvaEntities.addContainerFilter(new Compare.Equal("rate", 0.196));
            Tva tva196 = tvaEntities.getItem(tvaEntities.getItemIds().toArray()[0]).getEntity();
            tvaEntities.removeAllContainerFilters();

            tvaEntities.addContainerFilter(new Compare.Equal("rate", 0.2));
            Tva tva200 = tvaEntities.getItem(tvaEntities.getItemIds().toArray()[0]).getEntity();
            tvaEntities.removeAllContainerFilters();

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(0);
            calendar.set(2013, Calendar.JANUARY, 1);
            invoiceDAO.updateTVAByDefault(tva196, tva200, calendar.getTime());
        } catch (SQLException e) {
            Notification.show("Erreur de conversion de données", e.getMessage(), Notification.Type.ERROR_MESSAGE);
            LOG.error(e.getMessage(), e);
        }
    }

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = NavigatorUI.class)
    public static class Servlet extends VaadinServlet {

        /**
		 * 
		 */
		private static final long serialVersionUID = 5609038808232411088L;

		@Override
        public void destroy() {
            PersistenceManager.getInstance().closeEntityManagerFactory();
        }

    }

}
