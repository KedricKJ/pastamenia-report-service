package com.pastamenia.report;

/**
 * @author Pasindu Lakmal
 */
public interface DiscountDetailReport {

    String getCategory();

    String getOrderNo();

    String getOrderType();

    String getOrderTime();

    String getUserId();

    String getReason();

    Double getPercentage();

    double getSales();

    double getDiscount();

    double getTax();

    double getServiceCharge();
}
