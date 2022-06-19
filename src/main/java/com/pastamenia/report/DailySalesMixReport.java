package com.pastamenia.report;

/**
 * @author Pasindu Lakmal
 */
public interface DailySalesMixReport {
     String getItemId();
     String getItemName();
     String getVariantName();
     String getCategoryId();
     String getCategoryName();
     String getCreatedAt();
     Double getGrossTotalMoney();
     String getReceiptType();
     String getDiscountName();
     Double getDiscountAmount();

     Integer getQuantity();
}
