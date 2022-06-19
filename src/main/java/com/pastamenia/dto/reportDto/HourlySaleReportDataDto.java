package com.pastamenia.dto.reportDto;

import lombok.Data;

/**
 * @author Pasindu Lakmal
 */
@Data
public class HourlySaleReportDataDto {

    int transactions=0;
    int items=0;
    double avgSale;
    double sale;
    double percentageSale;


}
