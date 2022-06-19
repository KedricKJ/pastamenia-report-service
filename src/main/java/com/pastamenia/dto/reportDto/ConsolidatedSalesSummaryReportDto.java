package com.pastamenia.dto.reportDto;

import lombok.Data;

/**
 * @author Pasindu Lakmal
 */
@Data
public class ConsolidatedSalesSummaryReportDto {

    String store;

    String diningOption;

    double totalMoney;

    String payment;

    Integer count;

}
