package com.pastamenia.dto.reportDto;

import lombok.Data;

/**
 * @author Pasindu Lakmal
 */
@Data
public class HourlySaleReportDto {

    String createdAt;
    Double totalMoney;
    Integer lineItemId;
    Integer receiptNo;


}
