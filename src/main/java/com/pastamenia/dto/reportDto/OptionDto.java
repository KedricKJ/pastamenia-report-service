package com.pastamenia.dto.reportDto;

import lombok.Data;

import java.util.ArrayList;

/**
 * @author Pasindu Lakmal
 */
@Data
public class OptionDto {

    String optionName;
    ArrayList optionList;
    int itemsSold;
    double grossSale;
    int itemRefunded;
    double refunds;
    double discounts;
    double sumItemsSold;
    double sumGrossSale;
    int sumItemRefunded;
    double sumRefunds;
    double sumDiscounts;
    double sumNetSales;


}
