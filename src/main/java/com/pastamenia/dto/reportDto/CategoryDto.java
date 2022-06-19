package com.pastamenia.dto.reportDto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Pasindu Lakmal
 */
@Data
public class CategoryDto {

    String CategoryId;
    String Category;
    private Double netSale;
    private Double grossSale;
    private Integer itemCount;
    List<ItemDto> itemDtoList;
    Map<String, ItemDto> itemDtoMap;


}
