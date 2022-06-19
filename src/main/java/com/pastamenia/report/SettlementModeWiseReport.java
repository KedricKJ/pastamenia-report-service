package com.pastamenia.report;

/**
 * @author Pasindu Lakmal
 */
public interface SettlementModeWiseReport {
    String getSettlement();

    String getOrderNo();

    String getOrderType();

    String getCreatedAt();

    String getUserName();

    Double getSale();

    String getRemark();
}
