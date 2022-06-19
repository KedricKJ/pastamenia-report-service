package com.pastamenia.dto.reportDto;

import lombok.Data;

/**
 * @author Pasindu Lakmal
 */
@Data
public class SalesSummaryReportDto {

    String category;
    int quantity;
    double grossSale;
    double refunds;
    double totalDiscount;
    double grossTotal;
    double tax;
    double serviceCharge;
    double netTotal;
    double sale;

}
