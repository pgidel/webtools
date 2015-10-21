package com.bierocratie.ui.view.dashboard;

import com.bierocratie.bean.AccountingStatsBean;
import com.bierocratie.db.accounting.IncomeDAO;
import com.bierocratie.db.accounting.InvoiceDAO;
import com.bierocratie.model.accounting.BudgetYear;
import com.bierocratie.model.diffusion.MediumMessage;
import com.bierocratie.service.AccountingStatsService;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.component.Table;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 18/10/14
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
public class DashboardView extends VerticalLayout implements View {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardView.class);

    // TODO
    //@Inject
    private IncomeDAO incomeDAO = new IncomeDAO("dashboard");
    //@Inject
    private InvoiceDAO invoiceDAO = new InvoiceDAO("dashboard");

    private JPAContainer<MediumMessage> upcomingDiffusion;
    private Table upcomingDiffusionTable;

    /*private JPAContainer<Selection> selections;
    private Table selectionTable;*/

    /*private JPAContainer<Order> upcomingOrders;
    private Table upcomingOrderTable;*/

    /*private JPAContainer<Order> ordersInPreparation;
    private Table orderInPreparationTable;*/

    // FIXME @Inject
    //private MessageDAO messageDAO = new MessageDAO("dashboard");

    public DashboardView() {
        DashboardMenuBar dashboardMenuBar = new DashboardMenuBar();
        addComponent(dashboardMenuBar);

        GridLayout gridLayout = new GridLayout(3, 2);

        upcomingDiffusion = JPAContainerFactory.make(MediumMessage.class, "dashboard");
        upcomingDiffusion.addNestedContainerProperty("message.planned");
        upcomingDiffusion.sort(new Object[]{"date"}, new boolean[]{true});
        upcomingDiffusionTable = new Table("À diffuser", upcomingDiffusion);
        upcomingDiffusionTable.setVisibleColumns(new String[]{"message", "message.planned", "date", "medium"});
        upcomingDiffusionTable.setColumnHeader("message", "Message");
        upcomingDiffusionTable.setColumnHeader("message.planned", "Programmé");
        upcomingDiffusionTable.setColumnHeader("date", "Date");
        upcomingDiffusionTable.setColumnHeader("medium", "Medium");
        upcomingDiffusionTable.setSelectable(false);
        upcomingDiffusionTable.setPageLength(5);
        gridLayout.addComponent(upcomingDiffusionTable);

        try {
            JPAContainer<BudgetYear> budgetYearEntities = JPAContainerFactory.make(BudgetYear.class, "dashboard");
            if (!budgetYearEntities.getItemIds().isEmpty()) {
                budgetYearEntities.removeAllContainerFilters();
                budgetYearEntities.addContainerFilter(new Compare.LessOrEqual("firstMonth", BudgetYear.getCurrentMonth()));
                budgetYearEntities.addContainerFilter(new Compare.GreaterOrEqual("lastMonth", BudgetYear.getCurrentMonth()));
                BudgetYear budgetYear = budgetYearEntities.getItem(budgetYearEntities.firstItemId()).getEntity();

                gridLayout.addComponent(buildOperatingStatementPanel(budgetYear));
                gridLayout.addComponent(buildCashPlanPanel(budgetYear));
            }
        } catch (SQLException e) {
            Notification.show("Erreur d'accès aux données", e.getMessage(), Notification.Type.ERROR_MESSAGE);
            LOG.error(e.getMessage(), e);
        }

        /*upcomingOrders = JPAContainerFactory.make(Order.class, "dashboard");
        upcomingOrderTable = new Table("Commandes passées", upcomingOrders);
        upcomingOrderTable.setVisibleColumns(new String[]{"supplier"});
        upcomingOrderTable.setColumnHeader("supplier", "Fournisseur");
        upcomingOrderTable.setSelectable(false);
        upcomingOrderTable.setPageLength(5);
        gridLayout.addComponent(upcomingOrderTable);*/

        /*selections = JPAContainerFactory.make(Selection.class, "dashboard");
        selections.addNestedContainerProperty("beer.brewery");
        selections.sort(new Object[]{"date"}, new boolean[]{true});
        selectionTable = new Table("Hoppy Hours", selections);
        selectionTable.setVisibleColumns(new String[]{"date", "beer", "beer.brewery", "billingDate"});
        selectionTable.setColumnHeader("date", "Date");
        selectionTable.setColumnHeader("beer", "Bière");
        selectionTable.setColumnHeader("beer.brewery", "Brasserie");
        selectionTable.setColumnHeader("billingDate", "Réglées");
        selectionTable.setSelectable(false);
        selectionTable.setPageLength(5);
        gridLayout.addComponent(selectionTable);*/

        /*ordersInPreparation = JPAContainerFactory.make(Order.class, "dashboard");
        orderInPreparationTable = new Table("Commandes en préparation", ordersInPreparation);
        orderInPreparationTable.setVisibleColumns(new String[]{"supplier"});
        orderInPreparationTable.setColumnHeader("supplier", "Fournisseur");
        orderInPreparationTable.setSelectable(false);
        orderInPreparationTable.setPageLength(5);
        gridLayout.addComponent(orderInPreparationTable);*/

        addComponent(gridLayout);
    }

    private Panel buildCashPlanPanel(BudgetYear budgetYear) throws SQLException {
        Panel panel = new Panel("Plan de trésorerie (TTC)");
        VerticalLayout content = new VerticalLayout();
        panel.setContent(content);

        AccountingStatsBean accountingStatsBean = AccountingStatsService.getStats(budgetYear);

        TextField turnoverField = new TextField("Encaissements");
        turnoverField.setConverter(BigInteger.class);
        turnoverField.setValue(accountingStatsBean.getTurnoverTTC().toBigInteger().toString());
        turnoverField.setReadOnly(true);
        content.addComponent(turnoverField);

        TextField stockPurchaseField = new TextField("Décaissements");
        stockPurchaseField.setConverter(BigInteger.class);
        stockPurchaseField.setValue(accountingStatsBean.getInvoicesTTC().toBigInteger().toString());
        stockPurchaseField.setReadOnly(true);
        content.addComponent(stockPurchaseField);

        // TODO
        TextField deltaStockField = new TextField("Variation stock");
        deltaStockField.setConverter(BigInteger.class);
        deltaStockField.setValue(accountingStatsBean.getDeltaStock().multiply(new BigDecimal(1.2)).toBigInteger().toString());
        deltaStockField.setReadOnly(true);
        content.addComponent(deltaStockField);

        TextField marginField = new TextField("Marge");
        marginField.setConverter(BigInteger.class);
        marginField.setValue(accountingStatsBean.getMarginTTC().toBigInteger().toString());
        marginField.setReadOnly(true);
        content.addComponent(marginField);

        return panel;
    }

    private Panel buildOperatingStatementPanel(BudgetYear budgetYear) throws SQLException {
        Panel panel = new Panel("Compte de résultat (HT)");
        VerticalLayout content = new VerticalLayout();
        panel.setContent(content);

        AccountingStatsBean accountingStatsBean = AccountingStatsService.getStats(budgetYear);

        TextField turnoverField = new TextField("Produits");
        turnoverField.setConverter(BigInteger.class);
        turnoverField.setValue(accountingStatsBean.getTurnoverHT().toBigInteger().toString());
        turnoverField.setReadOnly(true);
        content.addComponent(turnoverField);

        TextField stockPurchaseField = new TextField("Charges");
        stockPurchaseField.setConverter(BigInteger.class);
        stockPurchaseField.setValue(accountingStatsBean.getInvoicesHT().toBigInteger().toString());
        stockPurchaseField.setReadOnly(true);
        content.addComponent(stockPurchaseField);

        TextField deltaStockField = new TextField("Variation stock");
        deltaStockField.setConverter(BigInteger.class);
        deltaStockField.setValue(accountingStatsBean.getDeltaStock().toBigInteger().toString());
        deltaStockField.setReadOnly(true);
        content.addComponent(deltaStockField);

        TextField marginField = new TextField("Marge");
        marginField.setConverter(BigInteger.class);
        marginField.setValue(accountingStatsBean.getMarginHT().toBigInteger().toString());
        marginField.setReadOnly(true);
        content.addComponent(marginField);

        TextField yieldField = new TextField("Taux de marque : (CA - Achat stock - Variation stock) / CA");
        yieldField.setValue(accountingStatsBean.getYieldRatio().toString() + "%");
        yieldField.setReadOnly(true);
        content.addComponent(yieldField);

        TextField marginRatioField = new TextField("Taux de marge : (CA - Achat stock - Variation stock) / Achat stock");
        marginRatioField.setValue(accountingStatsBean.getMarginRatio().toString() + "%");
        marginRatioField.setReadOnly(true);
        content.addComponent(marginRatioField);

        return panel;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        upcomingDiffusion.removeAllContainerFilters();
        upcomingDiffusion.addContainerFilter(new Compare.GreaterOrEqual("date", new Date()));

        /*selections.removeAllContainerFilters();
        selections.addContainerFilter(new Or(new Compare.GreaterOrEqual("date", new Date()), new Compare.Equal("billingDate", null)));*/

        /*upcomingOrders.removeAllContainerFilters();
        upcomingOrders.addContainerFilter(new Compare.Equal("ordered", true));*/

        /*ordersInPreparation.removeAllContainerFilters();
        ordersInPreparation.addContainerFilter(new Compare.Equal("ordered", false));*/
    }

}
