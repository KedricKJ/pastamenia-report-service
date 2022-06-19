package com.pastamenia.dto.reportDto;

import lombok.Data;

/**
 * @author Pasindu Lakmal
 */
@Data
public class DailySalesMixReportDto {

    String ItemId;
    String ItemName;
    String variantName;
    String CategoryId;
    String categoryName;
    String CreatedAt;
    Double GrossTotalMoney;
    String ReceiptType;
    String DiscountName;
    Double DiscountAmount;
    Integer quantity;
}
