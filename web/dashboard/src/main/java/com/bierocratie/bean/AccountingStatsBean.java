package com.bierocratie.bean;

import com.bierocratie.model.accounting.BudgetYear;
import com.bierocratie.model.accounting.Income;
import com.bierocratie.model.accounting.Tva;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.filter.Compare;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 16/10/15
 * Time: 19:56
 * To change this template use File | Settings | File Templates.
 */
public class AccountingStatsBean {

    private BigDecimal turnoverHT = BigDecimal.ZERO;
    private BigDecimal turnoverTTC = BigDecimal.ZERO;
    private BigDecimal invoicesHT = BigDecimal.ZERO;
    private BigDecimal invoicesTTC = BigDecimal.ZERO;
    private BigDecimal marginHT = BigDecimal.ZERO;
    private BigDecimal marginTTC = BigDecimal.ZERO;
    private BigDecimal yieldRatio = BigDecimal.ZERO;
    private BigDecimal marginRatio = BigDecimal.ZERO;
    private BigDecimal deltaStockHT = BigDecimal.ZERO;
    private BigDecimal deltaStockTTC = BigDecimal.ZERO;

    public AccountingStatsBean(BigDecimal turnoverHT, BigDecimal turnoverTTC, BigDecimal invoicesHT, BigDecimal invoicesTTC, BigDecimal currentStock, BigDecimal previousStock, BigDecimal stockPurchase) {
        this.turnoverHT = turnoverHT;
        this.turnoverTTC = turnoverTTC;
        this.invoicesHT = invoicesHT;
        this.invoicesTTC = invoicesTTC;

        final JPAContainer<Income> incomes = JPAContainerFactory.make(Income.class, "dashboard");
        incomes.addContainerFilter(new Compare.Equal("month", BudgetYear.getCurrentMonth()));
        while (incomes.size() == 0) {
            incomes.removeAllContainerFilters();

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);

            incomes.addContainerFilter(new Compare.Equal("month", BudgetYear.getMonth(cal.getTime())));
        }
        Tva tva = incomes.getItem(incomes.firstItemId()).getEntity().getTva();

        this.deltaStockHT = currentStock.subtract(previousStock);
        this.deltaStockTTC = this.deltaStockHT.multiply(BigDecimal.ONE.add(tva.getRate()));

        this.marginHT = turnoverHT.subtract(invoicesHT.subtract(deltaStockHT));
        this.marginTTC = turnoverTTC.subtract(invoicesTTC.subtract(deltaStockTTC));

        BigDecimal stockPurchaseMinusDelta = stockPurchase.subtract(deltaStockHT);
        BigDecimal grossMargin = turnoverHT.subtract(stockPurchaseMinusDelta);

        if (!turnoverHT.equals(BigDecimal.ZERO)) {
            this.yieldRatio = grossMargin.multiply(BigDecimal.valueOf(100)).divide(turnoverHT, 2, RoundingMode.HALF_UP);
        }
        if (!stockPurchase.equals(BigDecimal.ZERO)) {
            this.marginRatio = grossMargin.multiply(BigDecimal.valueOf(100)).divide(stockPurchase, 2, RoundingMode.HALF_UP);
        }
    }

    public BigDecimal getTurnoverHT() {
        return turnoverHT;
    }

    public BigDecimal getTurnoverTTC() {
        return turnoverTTC;
    }

    public BigDecimal getInvoicesHT() {
        return invoicesHT;
    }

    public BigDecimal getInvoicesTTC() {
        return invoicesTTC;
    }

    public BigDecimal getMarginHT() {
        return marginHT;
    }

    public BigDecimal getMarginTTC() {
        return marginTTC;
    }

    public BigDecimal getYieldRatio() {
        return yieldRatio;
    }

    public BigDecimal getMarginRatio() {
        return marginRatio;
    }

    public BigDecimal getDeltaStockHT() {
        return deltaStockHT;
    }

    public BigDecimal getDeltaStockTTC() {
        return deltaStockTTC;
    }

}
