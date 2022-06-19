package com.pastamenia.dto.response;

import lombok.Data;

@Data
public class DailySalesByVariantResponse {

  private String variantName;

  private Integer itemSold;

  private Double grossSale;

  private Double netValue;
}
