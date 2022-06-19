package com.pastamenia.dto.reportDto;

import lombok.Data;

/**
 * @author Pasindu Lakmal
 */
@Data
public class DiscountDetailReportDto {

    String category;

    String orderNo;

    String orderType;

    String orderTime;

    String userId;

    String reason;

    Double percentage;

    double sales;

    double discount;

    double tax;

    double serviceCharge;

    double total;

}
