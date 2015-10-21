package com.bierocratie.ui.view.accounting;

import com.bierocratie.db.accounting.IncomeDAO;
import com.bierocratie.db.accounting.InvoiceDAO;
import com.bierocratie.db.accounting.StockValueDAO;
import com.bierocratie.model.accounting.BudgetYear;
import com.bierocratie.model.accounting.Category;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.bierocratie.ui.component.OperatingStatementTab;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 18/10/14
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
public class OperatingStatementView extends VerticalLayout implements View {

    private static final Logger LOG = LoggerFactory.getLogger(OperatingStatementView.class);

    // TODO
    //@Inject
    private InvoiceDAO invoiceDAO = new InvoiceDAO("dashboard");
    //@Inject
    private IncomeDAO incomeDAO = new IncomeDAO("dashboard");
    //@Inject
    private StockValueDAO stockValueDAO = new StockValueDAO("dashboard");

    private JPAContainer<BudgetYear> budgetYears = JPAContainerFactory.make(BudgetYear.class, invoiceDAO.getPersistenceUnitName());
    private JPAContainer<Category> categories = JPAContainerFactory.make(Category.class, invoiceDAO.getPersistenceUnitName());

    private Map<String, BigInteger> incomesByMonth;
    private BigInteger incomesSum;
    private Map<String, BigInteger> outcomesByMonth;
    private BigInteger outcomesSum;
    private Map<Category, BigInteger> sumByCategory;

    public OperatingStatementView() {
        DashboardMenuBar dashboardMenuBar = new DashboardMenuBar();
        addComponent(dashboardMenuBar);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        try {
            TabSheet tabs = new TabSheet();
            budgetYears.sort(new Object[]{"year"}, new boolean[]{true});

            incomesByMonth = incomeDAO.getSumIncomesHTByMonth();
            outcomesByMonth = invoiceDAO.getSumInvoiceByMonthForOperating();

            for (Object id : budgetYears.getItemIds()) {
                incomesSum = BigInteger.ZERO;
                outcomesSum = BigInteger.ZERO;

                BudgetYear budgetYear = budgetYears.getItem(id).getEntity();

                OperatingStatementTab tab = new OperatingStatementTab(budgetYear, categories, incomesByMonth, outcomesByMonth);
                tabs.addTab(tab, budgetYear.getYear());
                if (budgetYear.isCurrentYear()) {
                    tabs.setSelectedTab(tab);
                }
            }
            addComponent(tabs);

            Button excelExportButton = new Button("Exporter sous Excel");
            excelExportButton.addClickListener(new Button.ClickListener() {
                public void buttonClick(final Button.ClickEvent event) {
                    OperatingStatementTab tab = (OperatingStatementTab) tabs.getTab(0).getComponent();
                    ExcelExport excelExport = new ExcelExport(tab.getTable(), tab.getBudgetYear().getYear());
                    String title = "Biérocratie - " + DashboardMenuBar.OPERATING_STATEMENT_TITLE;
                    excelExport.setReportTitle(title);
                    // FIXME Pouvoir changer le nom
                    // excelExport.setExportFileName(title);
                    excelExport.export();
                    for (int i = 1; i < tabs.getComponentCount(); i++) {
                        tab = (OperatingStatementTab) tabs.getTab(i).getComponent();
                        excelExport.setNextTable(tab.getTable(), tab.getBudgetYear().getYear());
                        excelExport.export();
                    }
                    excelExport.sendConverted();
                }
            });
            addComponent(excelExportButton);
        } catch (SQLException e) {
            Notification.show("Erreur d'accès aux données", e.getMessage(), Notification.Type.ERROR_MESSAGE);
            LOG.error(e.getMessage(), e);
        }
    }

}
