package com.pastamenia.dto.reportDto;

import lombok.Data;

/**
 * @author Pasindu Lakmal
 */
@Data
public class DailySalesModifierReportDto {

    String itemId;
    String category;
    String option;
    double price;
    String receiptType;
    double DiscountAmount;

}
