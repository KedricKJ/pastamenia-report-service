package com.pastamenia.dto.reportDto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Pasindu Lakmal
 */
@Data
public class ItemDto {

    String itemId;
    String itemName;
    Integer itemSoldCount;
    double grossSale;
    List<ItemTypeDto> itemTypeDtoList;
    Map<String, ItemTypeDto> itemTypeDtoMap;


}
