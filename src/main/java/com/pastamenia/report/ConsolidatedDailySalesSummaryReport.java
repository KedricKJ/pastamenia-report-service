package com.pastamenia.report;

/**
 * @author Pasindu Lakmal
 */
public interface ConsolidatedDailySalesSummaryReport {

    String getStore();

    String getDiningOption();

    double getTotalMoney();

    String getPayment();

    Integer getCount();
}
