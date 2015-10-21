package com.bierocratie.ui.view.accounting;

import com.bierocratie.db.accounting.InvoiceDAO;
import com.bierocratie.model.accounting.BudgetYear;
import com.bierocratie.model.accounting.Category;
import com.bierocratie.model.accounting.Invoice;
import com.bierocratie.ui.component.DashboardMenuBar;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import javax.persistence.Entity;
import java.io.File;
import java.io.FileFilter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings("serial")
public class InvoiceByCategoryView extends VerticalLayout implements View {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceByCategoryView.class);

    private final static String ACCOUNTING_PATH = ResourceBundle.getBundle("config").getString("accounting.root");
    private final static String ARCHIVE_PATH = ACCOUNTING_PATH + "+archives//";
    private final static String[] FOLDERS_TO_BE_IMPORTED = {"factures", "perso", "frais"};

    // TODO
    //@Inject
    private String persistenceUnitName = "dashboard";

    // TODO
    //@Inject
    private InvoiceDAO invoiceDAO = new InvoiceDAO(persistenceUnitName);

    private TabSheet tabs = new TabSheet();

    static {
        if (!new File(ACCOUNTING_PATH).exists()) {
            LOG.error("Le répertoire ["+ACCOUNTING_PATH+"] n'existe pas");
        }
    }

    private final static FileFilter PDF_FILTER = new FileFilter() {
        public boolean accept(File file) {
            return !file.isDirectory() && file.getName().endsWith(".pdf");
        }
    };

    private void importMonth(String month, JPAContainer<Invoice> entities) {
        if (Invoice.DEFAULT_MONTH.equals(month)) {
            importInvoicesByMonth(ACCOUNTING_PATH, month, entities);
        } else {
            for (String folder : FOLDERS_TO_BE_IMPORTED) {
                // /1501/1501-factures/
                importInvoicesByMonth(ARCHIVE_PATH + month + File.separator + month + "-" + folder, month, entities);
            }
        }
    }

    private void importInvoicesByMonth(String path, String month, JPAContainer<Invoice> entities) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] fileList = file.listFiles(PDF_FILTER);
            for (File f : fileList) {
                generateInvoice(f.getName(), month, entities);
            }
        }
    }

    private void generateInvoice(String s, String month, JPAContainer<Invoice> entities) {
        try {
            // [type_yymmdd_fournisseur-montant.pdf]
            s = s.replace(".pdf", "").replace(".PDF", "");
            String[] t = s.split("_")[1].split("-");
            Invoice invoice = new Invoice(t[0], t[1], t[2].split(" ")[0], month, true);
            Invoice i = invoiceDAO.find(invoice.getD(), invoice.getSupplier(), invoice.getAmount());
            if (i == null) {
                entities.addEntity(invoice);
            } else if (!month.equals(i.getMonth())) {
                i.setMonth(month);
                entities.addEntity(i);
            }
        } catch (SQLException e) {
            Notification.show("Erreur d'accès aux données", e.getMessage(), Notification.Type.ERROR_MESSAGE);
            LOG.error(e.getMessage(), e);
        }
    }

    private ComboBox monthsComboBox = new ComboBox("Mois");

    private List<String> generateMonthList() {
        File file = new File(ARCHIVE_PATH);
        if (!file.exists() || !file.isDirectory()) {
            return Collections.EMPTY_LIST;
        }

        List<String> months = new ArrayList<>();
        for (String m : file.list()) {
            // Past years (<n-1) are labelled 01, 02, etc.
            if (m.length() == 4) {
                months.add(m);
            }
        }
        Collections.reverse(months);
        return months;
    }

    // TODO
    //@Inject
    private JPAContainer<Invoice> invoiceEntities = JPAContainerFactory.make(Invoice.class, persistenceUnitName);

    // TODO
    //@Inject
    private JPAContainer<Category> categories = JPAContainerFactory.make(Category.class, "dashboard");

    // TODO
    //@Inject
    private JPAContainer<BudgetYear> budgetYears = JPAContainerFactory.make(BudgetYear.class, "dashboard");

    public InvoiceByCategoryView() {
        DashboardMenuBar dashboardMenuBar = new DashboardMenuBar();
        addComponent(dashboardMenuBar);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        HorizontalLayout importInvoiceLayout = new HorizontalLayout();

        monthsComboBox.addItem(Invoice.DEFAULT_MONTH);
        monthsComboBox.addItems(generateMonthList());
        monthsComboBox.setNullSelectionAllowed(false);
        monthsComboBox.setValue(Invoice.DEFAULT_MONTH);
        importInvoiceLayout.addComponent(monthsComboBox);

        Button importMonthButton = new Button("Importer", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                importMonth((String) monthsComboBox.getValue(), invoiceEntities);
            }
        });
        importInvoiceLayout.addComponent(importMonthButton);

        String currentMonth = BudgetYear.getCurrentMonth();
        budgetYears.addContainerFilter(new Compare.LessOrEqual("lastMonth", currentMonth));
        if (!budgetYears.getItemIds().isEmpty()) {

            addComponent(importInvoiceLayout);

            budgetYears.sort(new String[]{"year"}, new boolean[]{false});
            BudgetYear currentBudgetYear = budgetYears.getItem(budgetYears.firstItemId()).getEntity();

            Category emptyCategory = new Category();
            InvoiceView emptyInvoiceView = new InvoiceView(emptyCategory, currentBudgetYear);
            tabs.addTab(emptyInvoiceView, emptyCategory.getName());
            for (Object id : categories.getItemIds()) {
                Category category = categories.getItem(id).getEntity();
                InvoiceView invoiceView = new InvoiceView(category, currentBudgetYear);
                tabs.addTab(invoiceView, category.getName());
            }

            addComponent(tabs);
        }

        importMonth(Invoice.DEFAULT_MONTH, invoiceEntities);
    }

}
