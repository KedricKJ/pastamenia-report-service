package com.pastamenia.report;

/**
 * @author Pasindu Lakmal
 */
public interface HourlySaleReport {

    String getCreatedAt();

    Integer getReceiptNo();

    Double getTotalMoney();

    String getLineItemId();

}
