package com.pastamenia.dto.reportDto;

import lombok.Data;

/**
 * @author Pasindu Lakmal
 */
@Data
public class AmountDto {
    double amount;
    String description;
    int quantity;
    String reason;
}
