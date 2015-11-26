package com.bierocratie.ui.component;

import com.bierocratie.db.accounting.IncomeDAO;
import com.bierocratie.db.accounting.InvoiceDAO;
import com.bierocratie.model.accounting.BudgetYear;
import com.bierocratie.model.accounting.Category;
import com.bierocratie.model.accounting.CategoryAndMonth;
import com.bierocratie.model.accounting.Invoice;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Item;
import com.vaadin.ui.VerticalLayout;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 11/02/15
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */
public class CashPlanTab extends VerticalLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1662482594147865263L;
	// TODO
    //@Inject
    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    //@Inject
    private IncomeDAO incomeDAO = new IncomeDAO();

    private JPAContainer<Category> categories;

    private Map<String, BigInteger> incomesByMonth;
    private Map<String, BigInteger> outcomesByMonth;

    private Table table;

    private BudgetYear budgetYear;

    public CashPlanTab(BudgetYear budgetYear, JPAContainer<Category> categories, Map<String, BigInteger> incomesByMonth, Map<String, BigInteger> outcomesByMonth) throws SQLException {
        this.budgetYear = budgetYear;
        this.categories = categories;
        this.incomesByMonth = incomesByMonth;
        this.outcomesByMonth = outcomesByMonth;

        table = new Table(DashboardMenuBar.CASH_PLAN_TITLE);

        createTableContainerPropertiesAndItems();
        if (budgetYear.isCurrentYear() && !BudgetYear.getCurrentMonth().equals(budgetYear.getLastMonth())) {
            createCurrentAmountsColumn();
        }
        createTotalColumn();

        table.setFooterVisible(true);
        table.setPageLength(categories.size() + 3);

        addComponent(table);
    }

    public BudgetYear getBudgetYear() {
        return budgetYear;
    }

    public Table getTable() {
        return table;
    }

    private void createCurrentAmountsColumn() throws SQLException {
        table.addContainerProperty("currentSum", BigInteger.class, null);
        table.setColumnHeader("currentSum", "Total en cours");

        List<CategoryAndMonth> currentAmounts = invoiceDAO.getCurrentAmountsByYearForCash(budgetYear.getYear());
        BigInteger sumCurrentOutcomes = BigInteger.ZERO;
        for (CategoryAndMonth amount : currentAmounts) {
            Item categoryItem = table.getItem(amount.getCategory());
            BigInteger sum = amount.getAmount();
            BigInteger noMonth = (BigInteger) categoryItem.getItemProperty(Invoice.DEFAULT_MONTH).getValue();
            if (sum == null) {
                sum = BigInteger.ZERO;
            }
            if (noMonth == null) {
                noMonth = BigInteger.ZERO;
            }
            sum = sum.add(noMonth);
            categoryItem.getItemProperty("currentSum").setValue(sum);
            sumCurrentOutcomes = sumCurrentOutcomes.add(sum);
        }

        BigInteger sumCurrentIncomes = incomeDAO.getSumCurrentIncomesByYear(budgetYear.getYear()).toBigInteger();
        table.getItem("outcomeByMonth").getItemProperty("currentSum").setValue(sumCurrentOutcomes);
        table.getItem("incomeByMonth").getItemProperty("currentSum").setValue(sumCurrentIncomes);

        BigInteger profit;
        if (sumCurrentIncomes == null) {
            profit = sumCurrentOutcomes.negate();
        } else {
            profit = sumCurrentIncomes.subtract(sumCurrentOutcomes);
        }
        table.setColumnFooter("currentSum", Table.integerFormatter.format(profit.longValue()));
    }

    private void createTotalColumn() throws SQLException {
        table.addContainerProperty("sum", BigInteger.class, null);
        table.setColumnHeader("sum", "Total");

        List<CategoryAndMonth> amounts = invoiceDAO.getAmountsByYearForCash(budgetYear.getYear());
        BigInteger sumOutcomes = BigInteger.ZERO;
        for (CategoryAndMonth amount : amounts) {
            Item categoryItem = table.getItem(amount.getCategory());
            BigInteger sum = amount.getAmount();
            BigInteger noMonth = (BigInteger) categoryItem.getItemProperty(Invoice.DEFAULT_MONTH).getValue();
            if (sum == null) {
                sum = BigInteger.ZERO;
            }
            if (noMonth == null) {
                noMonth = BigInteger.ZERO;
            }
            sum = sum.add(noMonth);
            categoryItem.getItemProperty("sum").setValue(sum);
            sumOutcomes = sumOutcomes.add(sum);
        }
        BigInteger sumIncomes = incomeDAO.getSumIncomesByYear(budgetYear.getYear()).toBigInteger();
        table.getItem("outcomeByMonth").getItemProperty("sum").setValue(sumOutcomes);
        table.getItem("incomeByMonth").getItemProperty("sum").setValue(sumIncomes);

        BigInteger profit;
        if (sumIncomes == null) {
            profit = sumOutcomes.negate();
        } else {
            profit = sumIncomes.subtract(sumOutcomes);
        }
        table.setColumnFooter("sum", Table.integerFormatter.format(profit.longValue()));
    }

    private void createTableContainerPropertiesAndItems() throws SQLException {
        table.addContainerProperty("category", String.class, null);
        table.setColumnHeader("category", "Catégorie");

        Item incomeByMonthItem = table.addItem("incomeByMonth");
        incomeByMonthItem.getItemProperty("category").setValue("--- Recettes ---");
        for (Object id : categories.getItemIds()) {
            Category category = categories.getItem(id).getEntity();
            Item categoryItem = table.addItem(category);
            categoryItem.getItemProperty("category").setValue(category.getName());
        }

        Category emptyCategory = new Category();
        Item categoryItem = table.addItem(emptyCategory);
        categoryItem.getItemProperty("category").setValue(emptyCategory.getName());

        Item outcomeByMonthItem = table.addItem("outcomeByMonth");
        outcomeByMonthItem.getItemProperty("category").setValue("--- Dépenses ---");

        List<String> months = invoiceDAO.getMonthsByYearForCash(budgetYear.getYear());
        if (budgetYear.isCurrentYear()) {
            months.add(Invoice.DEFAULT_MONTH);
        }
        for (String month : months) {
            createColumnForMonth(month, incomeByMonthItem, outcomeByMonthItem);
        }
    }

    private void createColumnForMonth(String month, Item incomeByMonthItem, Item outcomeByMonthItem) throws SQLException {
        table.addContainerProperty(month, BigInteger.class, null);

        List<CategoryAndMonth> amounts = invoiceDAO.getAmountsByMonthForCash(month);
        for (CategoryAndMonth amount : amounts) {
            Item categoryItem = table.getItem(amount.getCategory());
            categoryItem.getItemProperty(amount.getMonth()).setValue(amount.getAmount());
        }

        if (outcomesByMonth.get(month) != null) {
            outcomeByMonthItem.getItemProperty(month).setValue(outcomesByMonth.get(month));
        }

        incomeByMonthItem.getItemProperty(month).setValue(incomesByMonth.get(month));
        if (incomesByMonth.get(month) != null) {
            incomeByMonthItem.getItemProperty(month).setValue(incomesByMonth.get(month));
        }

        BigInteger profit = BigInteger.ZERO;
        if (outcomesByMonth.get(month) != null) {
            if (incomesByMonth.get(month) == null) {
                profit = outcomesByMonth.get(month).negate();
            } else {
                profit = incomesByMonth.get(month).subtract(outcomesByMonth.get(month));
            }
        }
        table.setColumnFooter(month, Table.integerFormatter.format(profit.longValue()));
    }

}
