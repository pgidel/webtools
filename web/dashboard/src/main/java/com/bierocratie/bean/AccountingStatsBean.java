package com.bierocratie.bean;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
    private BigDecimal deltaStock = BigDecimal.ZERO;

    public AccountingStatsBean(BigDecimal turnoverHT, BigDecimal turnoverTTC, BigDecimal invoicesHT, BigDecimal invoicesTTC, BigDecimal currentStock, BigDecimal previousStock, BigDecimal stockPurchase) {
        this.turnoverHT = turnoverHT;
        this.turnoverTTC = turnoverTTC;
        this.invoicesHT = invoicesHT;
        this.invoicesTTC = invoicesTTC;

        this.deltaStock = currentStock.subtract(previousStock);

        this.marginHT = turnoverHT.subtract(invoicesHT.subtract(deltaStock));
        this.marginTTC = turnoverTTC.subtract(invoicesTTC.subtract(deltaStock.multiply(new BigDecimal(1.2))));

        BigDecimal stockPurchaseMinusDelta = stockPurchase.subtract(deltaStock);
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

    public BigDecimal getDeltaStock() {
        return deltaStock;
    }

}
