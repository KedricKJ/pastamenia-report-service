package com.pastamenia.dto.reportDto;

import lombok.Data;

/**
 * @author Pasindu Lakmal
 */
@Data
public class ItemTypeDto {

    private String variantName;
    private int itemSoldCount;
    private double grossSale;
    private int refundedCount;
    private double refundedAmount;
    private double discount;
    private double netTotalSale;



}
