package com.pastamenia.report;

import java.math.BigDecimal;

/**
 * @author Pasindu Lakmal
 */
public interface SaleSummaryReport {
    String getCategory();

    Integer getQuantity();

    Double getGrossSale();

    Double getRefunds();

    Double getTotalDiscount();

    Double getGrossTotal();

    BigDecimal getTax();

    Double getServiceCharge();

    Double getNetTotal();


}
