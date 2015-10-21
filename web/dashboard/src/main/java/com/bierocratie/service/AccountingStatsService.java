package com.bierocratie.service;

import com.bierocratie.bean.AccountingStatsBean;
import com.bierocratie.db.accounting.IncomeDAO;
import com.bierocratie.db.accounting.InvoiceDAO;
import com.bierocratie.db.accounting.StockValueDAO;
import com.bierocratie.model.accounting.BudgetYear;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 16/10/15
 * Time: 19:48
 * To change this template use File | Settings | File Templates.
 */
public class AccountingStatsService {

    // TODO
    //@Inject
    private static StockValueDAO stockValueDAO = new StockValueDAO("dashboard");
    //@Inject
    private static InvoiceDAO invoiceDAO = new InvoiceDAO("dashboard");
    //@Inject
    private static IncomeDAO incomeDAO = new IncomeDAO("dashboard");

    public static AccountingStatsBean getStats(BudgetYear budgetYear) throws SQLException {
        BigDecimal turnoverHT = incomeDAO.getSumIncomesHTByYear(budgetYear.getYear());
        BigDecimal turnoverTTC = incomeDAO.getSumIncomesByYear(budgetYear.getYear());

        BigDecimal invoicesHT = invoiceDAO.getSumCurrentInvoicesHTByYear(budgetYear.getYear());
        BigDecimal invoicesTTC = invoiceDAO.getSumCurrentInvoicesByYear(budgetYear.getYear());

        BigDecimal currentStock = stockValueDAO.getStockHTByYear(budgetYear.getYear());
        BigDecimal previousStock = stockValueDAO.getStockHTByYear(String.valueOf(Integer.parseInt(budgetYear.getYear()) - 1));
        BigDecimal stockPurchase = invoiceDAO.getSumStockInvoicesHTByYear(budgetYear.getYear());

        return new AccountingStatsBean(turnoverHT, turnoverTTC, invoicesHT, invoicesTTC, currentStock, previousStock, stockPurchase);
    }

}
