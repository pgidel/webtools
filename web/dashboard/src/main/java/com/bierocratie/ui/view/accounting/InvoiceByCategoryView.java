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

import java.io.File;
import java.io.FileFilter;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("serial")
public class InvoiceByCategoryView extends VerticalLayout implements View {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceByCategoryView.class);

    private final static String ACCOUNTING_PATH = ResourceBundle.getBundle("config").getString("accounting.root");
    private final static String ARCHIVE_PATH = ACCOUNTING_PATH + "+archives//";
    private final static String[] FOLDERS_TO_BE_IMPORTED = {"factures", "perso", "frais"};

    private final static String ALL_MONTH = "*";

    private List<String> monthList;

    private InvoiceDAO invoiceDAO = new InvoiceDAO();

    // TODO
    //@Inject
    private JPAContainer<Invoice> invoiceEntities = JPAContainerFactory.make(Invoice.class, "dashboard");

    private ComboBox monthsComboBox = new ComboBox("Mois");

    private TabSheet tabs = new TabSheet();

    private Map<String, InvoiceView> invoiceViewByCategoryMap = new HashMap<>();

    private final static FileFilter PDF_FILTER = new FileFilter() {
        public boolean accept(File file) {
            return !file.isDirectory() && file.getName().endsWith(".pdf");
        }
    };

    private void importMonth(String month) {
        if (Invoice.DEFAULT_MONTH.equals(month)) {
            importInvoicesByMonth(ACCOUNTING_PATH, month);

        }
        if (ALL_MONTH.equals(month)) {
            for (String m : monthList) {
                importFolders(m);
            }

        } else {
            importFolders(month);
        }
        for (Map.Entry<String, InvoiceView> entry : invoiceViewByCategoryMap.entrySet()) {
            invoiceViewByCategoryMap.get(entry.getKey()).getEntities().refresh();
        }
    }

    private void importFolders(String month) {
        for (String folder : FOLDERS_TO_BE_IMPORTED) {
            // /1501/1501-factures/
            importInvoicesByMonth(ARCHIVE_PATH + month + File.separator + month + "-" + folder, month);
        }
    }

    private void importInvoicesByMonth(String path, String month) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] fileList = file.listFiles(PDF_FILTER);
            for (File f : fileList) {
                generateInvoice(f.getName(), month);
            }
        }
    }

    private void generateInvoice(String s, String month) {
        try {
            // [type_yymmdd_fournisseur-montant.pdf]
            s = s.replace(".pdf", "").replace(".PDF", "");
            String[] t = s.split("_")[1].split("-");
            Invoice invoice = new Invoice(t[0], t[1], t[2].split(" ")[0], month, true);
            Invoice i = invoiceDAO.find(invoice.getD(), invoice.getSupplier(), invoice.getAmount());
            if (i == null) {
                invoiceEntities.addEntity(invoice);
            } else if (!month.equals(i.getMonth())) {
                i.setMonth(month);
                invoiceEntities.addEntity(i);
            }
        } catch (SQLException e) {
            Notification.show("Erreur d'accès aux données", e.getMessage(), Notification.Type.ERROR_MESSAGE);
            LOG.error(e.getMessage(), e);
        }
    }

    private List<String> generateMonthList() {
        File file = new File(ARCHIVE_PATH);
        if (!file.exists() || !file.isDirectory()) {
            return Collections.EMPTY_LIST;
        }

        List<String> months = new ArrayList<>();
        for (String m : file.list()) {
            // Past years (<n-1) are labelled 01-xxxx, 02-xxxx, etc.
            if (m.length() == 4) {
                months.add(m);
            }
        }
        Collections.reverse(months);
        return months;
    }

    private Button importMonthButton = new Button("Importer", new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            importMonth((String) monthsComboBox.getValue());
        }
    });

    public InvoiceByCategoryView() {
        DashboardMenuBar dashboardMenuBar = new DashboardMenuBar();
        addComponent(dashboardMenuBar);

        HorizontalLayout importInvoiceLayout = new HorizontalLayout();

        monthsComboBox.setNullSelectionAllowed(false);
        importInvoiceLayout.addComponent(monthsComboBox);

        importInvoiceLayout.addComponent(importMonthButton);

        String currentMonth = BudgetYear.getCurrentMonth();

        // TODO
        //@Inject
        JPAContainer<BudgetYear> budgetYears = JPAContainerFactory.make(BudgetYear.class, "dashboard");
        budgetYears.addContainerFilter(new Compare.LessOrEqual("lastMonth", currentMonth));
        if (!budgetYears.getItemIds().isEmpty()) {

            addComponent(importInvoiceLayout);

            budgetYears.sort(new String[]{"year"}, new boolean[]{false});
            BudgetYear currentBudgetYear = budgetYears.getItem(budgetYears.firstItemId()).getEntity();

            // TODO
            //@Inject
            JPAContainer<Category> categories = JPAContainerFactory.make(Category.class, "dashboard");

            Category emptyCategory = new Category();
            addTab(emptyCategory, currentBudgetYear);
            for (Object id : categories.getItemIds()) {
                Category category = categories.getItem(id).getEntity();
                addTab(category, currentBudgetYear);
            }

            addComponent(tabs);
        }
    }

    private void addTab(Category category, BudgetYear currentBudgetYear) {
        InvoiceView invoiceView = new InvoiceView(category, currentBudgetYear, this);
        tabs.addTab(invoiceView, category.getName());
        invoiceViewByCategoryMap.put(category.getName(), invoiceView);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if (!new File(ACCOUNTING_PATH).exists()) {
            LOG.error("Le répertoire [{}] n'existe pas", ACCOUNTING_PATH);
            Notification.show("Le répertoire [" + ACCOUNTING_PATH + "] n'existe pas", Notification.Type.ERROR_MESSAGE);

            setImportEnable(false);

        } else {
            monthList = generateMonthList();

            setImportEnable(true);

            monthsComboBox.removeAllItems();
            monthsComboBox.addItem(Invoice.DEFAULT_MONTH);
            monthsComboBox.addItems(monthList);
            monthsComboBox.addItems(ALL_MONTH);
            monthsComboBox.setValue(Invoice.DEFAULT_MONTH);
            monthsComboBox.setPageLength(monthList.size() + 2);
        }
    }

    private void setImportEnable(boolean enable) {
        importMonthButton.setEnabled(enable);
        monthsComboBox.setEnabled(enable);
    }

    public Map<String, InvoiceView> getInvoiceViewByCategoryMap() {
        return invoiceViewByCategoryMap;
    }

}
