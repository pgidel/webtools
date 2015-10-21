package com.bierocratie.ui.component;

import com.bierocratie.bean.AccountingStatsBean;
import com.bierocratie.db.accounting.IncomeDAO;
import com.bierocratie.db.accounting.InvoiceDAO;
import com.bierocratie.db.accounting.StockValueDAO;
import com.bierocratie.model.accounting.BudgetYear;
import com.bierocratie.model.accounting.Category;
import com.bierocratie.model.accounting.CategoryAndMonth;
import com.bierocratie.service.AccountingStatsService;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Item;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
public class OperatingStatementTab extends VerticalLayout {

    // TODO
    //@Inject
    private InvoiceDAO invoiceDAO = new InvoiceDAO("dashboard");
    //@Inject
    private IncomeDAO incomeDAO = new IncomeDAO("dashboard");

    private JPAContainer<Category> categories;

    private Map<String, BigInteger> incomesByMonth;
    private Map<String, BigInteger> outcomesByMonth;

    private Table table;

    private BudgetYear budgetYear;

    public OperatingStatementTab(BudgetYear budgetYear, JPAContainer<Category> categories, Map<String, BigInteger> incomesByMonth, Map<String, BigInteger> outcomesByMonth) throws SQLException {
        this.budgetYear = budgetYear;
        this.categories = categories;
        this.incomesByMonth = incomesByMonth;
        this.outcomesByMonth = outcomesByMonth;

        table = new Table(DashboardMenuBar.OPERATING_STATEMENT_TITLE);

        AccountingStatsBean accountingStatsBean = AccountingStatsService.getStats(budgetYear);

        createTableContainerPropertiesAndItems();
        Item stockByMonthItem = table.addItem("stockByMonth");
        stockByMonthItem.getItemProperty("category").setValue("--- Delta stocks ---");
        if (budgetYear.isCurrentYear() && !BudgetYear.getCurrentMonth().equals(budgetYear.getLastMonth())) {
            createCurrentAmountsColumn(accountingStatsBean.getDeltaStock().toBigInteger());
        }
        createTotalColumn(accountingStatsBean.getDeltaStock().toBigInteger());

        table.setFooterVisible(true);
        table.setPageLength(0);

        addComponent(table);

        TextField yieldField = new TextField("Taux de marque : (CA - Achat stock - Variation stock) / CA");
        yieldField.setValue(accountingStatsBean.getYieldRatio().toString() + "%");
        yieldField.setReadOnly(true);
        addComponent(yieldField);

        TextField marginRatioField = new TextField("Taux de marge : (CA - Achat stock - Variation stock) / Achat stock");
        marginRatioField.setValue(accountingStatsBean.getMarginRatio().toString() + "%");
        marginRatioField.setReadOnly(true);
        addComponent(marginRatioField);
    }

    public BudgetYear getBudgetYear() {
        return budgetYear;
    }

    public Table getTable() {
        return table;
    }

    private void createCurrentAmountsColumn(BigInteger deltaStock) throws SQLException {
        table.addContainerProperty("currentSum", BigInteger.class, null);
        table.setColumnHeader("currentSum", "Total en cours");

        List<CategoryAndMonth> currentAmounts = invoiceDAO.getCurrentAmountsByYearForOperating(budgetYear.getYear());
        BigInteger sumCurrentOutcomes = BigInteger.ZERO;
        for (CategoryAndMonth amount : currentAmounts) {
            Item categoryItem = table.getItem(amount.getCategory());
            categoryItem.getItemProperty("currentSum").setValue(amount.getAmount());
            sumCurrentOutcomes = sumCurrentOutcomes.add(amount.getAmount());
        }

        BigInteger sumCurrentIncomes = incomeDAO.getSumCurrentIncomesHTByYear(budgetYear.getYear()).toBigInteger();
        table.getItem("outcomeByMonth").getItemProperty("currentSum").setValue(sumCurrentOutcomes);
        table.getItem("incomeByMonth").getItemProperty("currentSum").setValue(sumCurrentIncomes);

        table.getItem("stockByMonth").getItemProperty("currentSum").setValue(deltaStock);

        BigInteger profit;
        if (sumCurrentIncomes == null) {
            profit = sumCurrentOutcomes.negate();
        } else {
            profit = sumCurrentIncomes.subtract(sumCurrentOutcomes);
        }
        table.setColumnFooter("currentSum", Table.integerFormatter.format(profit.longValue()));
        table.setColumnFooter("currentSum", Table.integerFormatter.format(profit.add(deltaStock).longValue()));
    }

    private void createTotalColumn(BigInteger deltaStock) throws SQLException {
        table.addContainerProperty("sum", BigInteger.class, null);
        table.setColumnHeader("sum", "Total");

        List<CategoryAndMonth> amounts = invoiceDAO.getAmountsByYearForOperating(budgetYear.getYear());
        BigInteger sumOutcomes = BigInteger.ZERO;
        for (CategoryAndMonth amount : amounts) {
            Item categoryItem = table.getItem(amount.getCategory());
            BigInteger sum = amount.getAmount();
            if (sum == null) {
                sum = BigInteger.ZERO;
            }
            categoryItem.getItemProperty("sum").setValue(sum);
            sumOutcomes = sumOutcomes.add(sum);
        }

        BigInteger sumIncomes = incomeDAO.getSumIncomesHTByYear(budgetYear.getYear()).toBigInteger();
        table.getItem("outcomeByMonth").getItemProperty("sum").setValue(sumOutcomes);
        table.getItem("incomeByMonth").getItemProperty("sum").setValue(sumIncomes);

        table.getItem("stockByMonth").getItemProperty("sum").setValue(deltaStock);

        BigInteger profit;
        if (sumIncomes == null) {
            profit = sumOutcomes.negate();
        } else {
            profit = sumIncomes.subtract(sumOutcomes);
        }
        table.setColumnFooter("sum", Table.integerFormatter.format(profit.longValue()));
        table.setColumnFooter("sum", Table.integerFormatter.format(profit.add(deltaStock).longValue()));
    }

    private void createTableContainerPropertiesAndItems() throws SQLException {
        table.addContainerProperty("category", String.class, null);
        table.setColumnHeader("category", "Catégorie");

        Item incomeByMonthItem = table.addItem("incomeByMonth");
        incomeByMonthItem.getItemProperty("category").setValue("--- Produits ---");
        for (Object id : categories.getItemIds()) {
            Category category = categories.getItem(id).getEntity();
            Item categoryItem = table.addItem(category);
            categoryItem.getItemProperty("category").setValue(category.getName());
        }

        Category emptyCategory = new Category();
        Item categoryItem = table.addItem(emptyCategory);
        categoryItem.getItemProperty("category").setValue(emptyCategory.getName());

        Item outcomeByMonthItem = table.addItem("outcomeByMonth");
        outcomeByMonthItem.getItemProperty("category").setValue("--- Charges ---");

        List<String> months = invoiceDAO.getMonthsByYearForOperating(budgetYear.getYear());
        for (String month : months) {
            createColumnForMonth(month, incomeByMonthItem, outcomeByMonthItem);
        }
    }

    private void createColumnForMonth(String month, Item incomeByMonthItem, Item outcomeByMonthItem) throws SQLException {
        table.addContainerProperty(month, BigInteger.class, null);

        List<CategoryAndMonth> amounts = invoiceDAO.getAmountsHTByMonthForOperating(month);
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
