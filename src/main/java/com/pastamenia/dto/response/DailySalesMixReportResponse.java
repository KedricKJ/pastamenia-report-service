package com.pastamenia.dto.response;

import com.pastamenia.dto.reportDto.DailySalesMixReportDto;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DailySalesMixReportResponse {

  private String categoryName;
  private String itemDescription;
  private Integer itemSold;
  private Double grossSale;

  List<DailySalesByVariantResponse> dailySalesByVariantResponses;

  Map<String, List<DailySalesMixReportDto>> variantMap;

  Map<String, Integer> itemSoldByVariant;

  private List<ItemData> items;

  private List<ItemVariantData> itemVariants;

  @Data
  public static class ItemVariantData {

    private String name;


  }

  @Data
  public static class ItemData {

    private String name;

    private Integer itemSold;

    private Double grossSale;

    private Double netSale;


  }
}
