package com.pastamenia.report;

/**
 * @author Pasindu Lakmal
 */
public interface VoidRefundSalesSummaryReport {
    double getAmount();

    String getDescription();

    int getQuantity();

    String getReason();
}
