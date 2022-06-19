package com.pastamenia.report;

/**
 * @author Pasindu Lakmal
 */
public interface VoidRefundDetailReport {
    String getType();

    String getReceiptType();

    String getReason();

    String getOrderNo();

    String getCreatedAt();

    String getUserName();

    Double getSale();

    String getOrderType();

    String getAuthorizedId();


}
