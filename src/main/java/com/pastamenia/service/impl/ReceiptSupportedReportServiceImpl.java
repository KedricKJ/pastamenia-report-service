package com.pastamenia.service.impl;

import com.pastamenia.Repository.ReceiptRepository;
import com.pastamenia.dto.reportDto.*;
import com.pastamenia.dto.response.*;
import com.pastamenia.entity.Company;
import com.pastamenia.modelmapper.ModelMapper;
import com.pastamenia.report.*;
import com.pastamenia.service.ReceiptSupportedReportService;
import com.pastamenia.util.Formatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

/**
 * @author Pasindu Lakmal
 */
@Service
@Transactional
@Slf4j
public class ReceiptSupportedReportServiceImpl implements ReceiptSupportedReportService {

    @Autowired
    ReceiptRepository receiptRepository;

    @Autowired
    ModelMapper modelMapper;


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public DailySaleMixReportModifiersReportResponse parseThymeleafTemplateForDailySaleMixReportModifiersReport(Company company, String from, String to) {

        Map<String, List<OptionDto>> modifierDtoMap = getModifierDtoMap(from, to, company.getId());

        DailySaleMixReportModifiersReportResponse response = new DailySaleMixReportModifiersReportResponse();
        response.setCategoryList(modifierDtoMap);
        response.setCompanyName(company.getName());
        response.setDate(LocalDate.now());

        /*TemplateEngine templateEngine = getTemplateEngine();
        Context context = new Context();
        context.setVariable("categoryList", modifierDtoMap);
        context.setVariable("companyName", company.getName());
        context.setVariable("date", LocalDate.now());*/
        return response;

    }




    @Override
    public DailySalesSummeryReportResponse parseThymeleafTemplateForSalesSummaryReport(Company company , String from , String to) {

       //DiningType
        List<SaleSummaryReport> dataForSaleSummaryReportDiningType = receiptRepository.findDataForSaleSummaryReportDiningType(from, to,company.getId());
        List<SaleSummaryReportDineInVoidsAndRefunds> dataForSaleSummaryReportDineInVoidsAndRefunds = receiptRepository.findSaleSummaryReportDineInVoidsAndRefunds(from, to,company.getId());
        List<SalesSummaryReportDto> dataForSaleSummaryReportDiningTypeDto = modelMapper.map(dataForSaleSummaryReportDiningType, SalesSummaryReportDto.class);
        List<SalesSummaryReportDto> dataForSaleSummaryReportDiningTypeDtoVoidsAndRefunds = modelMapper.map(dataForSaleSummaryReportDineInVoidsAndRefunds, SalesSummaryReportDto.class);

        //Category
        List<SaleSummaryReport> dataForSaleSummaryReportCategory = receiptRepository.findDataForSaleSummaryReportCategory(from, to, company.getId());

        List<SaleSummaryReportDineInVoidsAndRefunds> saleSummaryReportCategoryVoidsAndRefunds = receiptRepository.findSaleSummaryReportCategoryVoidsAndRefunds(from, to, company.getId());
        List<SalesSummaryReportDto> dataForSaleSummaryReportCategoryDto = modelMapper.map(dataForSaleSummaryReportCategory, SalesSummaryReportDto.class);
        List<SalesSummaryReportDto> salesSummaryReportCategoryDtoVoidsAndRefunds = modelMapper.map(saleSummaryReportCategoryVoidsAndRefunds, SalesSummaryReportDto.class);

        //PaymentMode
        List<SaleSummaryReport> dataForSaleSummaryReportPaymentMode = receiptRepository.findDataForSaleSummaryReportPaymentMode(from, to, company.getId());
        List<SaleSummaryReportDineInVoidsAndRefunds> saleSummaryReportPaymentTypeVoidsAndRefunds = receiptRepository.findSaleSummaryReportPaymentTypeVoidsAndRefunds(from, to, company.getId());
        List<SalesSummaryReportDto> dataForSaleSummaryReportPaymentModeDto = modelMapper.map(dataForSaleSummaryReportPaymentMode, SalesSummaryReportDto.class);
        List<SalesSummaryReportDto> salesSummaryReportPaymentTypeDtoVoidsAndRefunds = modelMapper.map(saleSummaryReportPaymentTypeVoidsAndRefunds, SalesSummaryReportDto.class);


        //DiningType
        dataForSaleSummaryReportDiningTypeDto.forEach(dto ->{
            double voidsAndRefunds = dataForSaleSummaryReportDiningTypeDtoVoidsAndRefunds.stream().filter(val -> val.getCategory().equalsIgnoreCase(dto.getCategory())).collect(Collectors.toList()).stream().map(cat -> cat.getRefunds()).mapToDouble(d -> d).sum();
            dto.setRefunds(Formatter.round((voidsAndRefunds),2));
            dto.setGrossTotal(Formatter.round((dto.getGrossSale()-(voidsAndRefunds+dto.getTotalDiscount())),2));
            dto.setNetTotal(Formatter.round((dto.getGrossTotal()-(dto.getTax()+dto.getServiceCharge())),2));

        });

        double sumDineIn = dataForSaleSummaryReportDiningTypeDto.stream().mapToDouble(val -> val.getNetTotal()).sum();
        dataForSaleSummaryReportDiningTypeDto.forEach(dto ->{ dto.setSale(Formatter.round(((dto.getNetTotal()/sumDineIn)*100),2));});

        int totalQuantityDiningType = dataForSaleSummaryReportDiningTypeDto.stream().mapToInt(i-> i.getQuantity()).sum();
        double totalGrossSaleDiningType =  Formatter.round(dataForSaleSummaryReportDiningTypeDto.stream().mapToDouble(i-> i.getGrossSale()).sum(),2);
        double totalDiscountDiningType =    Formatter.round(dataForSaleSummaryReportDiningTypeDto.stream().mapToDouble(i-> i.getTotalDiscount()).sum(),2);
        double grossTotalDiningType =  Formatter.round(dataForSaleSummaryReportDiningTypeDto.stream().mapToDouble(i-> i.getGrossTotal()).sum(),2);
        double totalTaxDiningType =  Formatter.round(dataForSaleSummaryReportDiningTypeDto.stream().mapToDouble(i-> i.getTax()).sum(),2);
        double totalServiceChargeDiningType =   Formatter.round(dataForSaleSummaryReportDiningTypeDto.stream().mapToDouble(i-> i.getServiceCharge()).sum(),2);
        double totalNetTotalDiningType =  Formatter.round(dataForSaleSummaryReportDiningTypeDto.stream().mapToDouble(i-> i.getNetTotal()).sum(),2);
        double totalSaleDiningType =  Formatter.round(dataForSaleSummaryReportDiningTypeDto.stream().mapToDouble(i-> i.getSale()).sum(),2);


        //Category
        dataForSaleSummaryReportCategoryDto.forEach(dto ->{
            double voidsAndRefunds = salesSummaryReportCategoryDtoVoidsAndRefunds.stream().filter(val -> val.getCategory().equalsIgnoreCase(dto.getCategory())).collect(Collectors.toList()).stream().map(cat -> cat.getRefunds()).mapToDouble(d -> d).sum();
            dto.setRefunds(Formatter.round((voidsAndRefunds),2));
            dto.setGrossTotal(Formatter.round((dto.getGrossSale()-(voidsAndRefunds+dto.getTotalDiscount())),2));
            dto.setNetTotal(Formatter.round((dto.getGrossTotal()-(dto.getTax()+dto.getServiceCharge())),2));
        });

        double sumCategory = dataForSaleSummaryReportCategoryDto.stream().mapToDouble(val -> val.getNetTotal()).sum();
        dataForSaleSummaryReportCategoryDto.forEach(dto1 ->{ dto1.setSale(Formatter.round(((dto1.getNetTotal()/sumCategory)*100),2));});

        int totalQuantityCategory = dataForSaleSummaryReportDiningTypeDto.stream().mapToInt(i-> i.getQuantity()).sum();
        double totalGrossSaleCategory =  Formatter.round(dataForSaleSummaryReportDiningTypeDto.stream().mapToDouble(i-> i.getGrossSale()).sum(),2);
        double totalDiscountCategory =    Formatter.round(dataForSaleSummaryReportDiningTypeDto.stream().mapToDouble(i-> i.getTotalDiscount()).sum(),2);
        double grossTotalCategory =  Formatter.round(dataForSaleSummaryReportDiningTypeDto.stream().mapToDouble(i-> i.getGrossTotal()).sum(),2);
        double totalTaxCategory =  Formatter.round(dataForSaleSummaryReportDiningTypeDto.stream().mapToDouble(i-> i.getTax()).sum(),2);
        double totalServiceChargeCategory =   Formatter.round(dataForSaleSummaryReportDiningTypeDto.stream().mapToDouble(i-> i.getServiceCharge()).sum(),2);
        double totalNetTotalCategory =  Formatter.round(dataForSaleSummaryReportDiningTypeDto.stream().mapToDouble(i-> i.getNetTotal()).sum(),2);
        double totalSaleCategory =  Formatter.round(dataForSaleSummaryReportDiningTypeDto.stream().mapToDouble(i-> i.getSale()).sum(),2);


        //PaymentMode
        dataForSaleSummaryReportPaymentModeDto.forEach(dto ->{
            double voidsAndRefunds = salesSummaryReportPaymentTypeDtoVoidsAndRefunds.stream().filter(val -> val.getCategory().equalsIgnoreCase(dto.getCategory())).collect(Collectors.toList()).stream().map(cat -> cat.getRefunds()).mapToDouble(d -> d).sum();
            dto.setRefunds(Formatter.round((voidsAndRefunds),2));
            dto.setGrossTotal(Formatter.round((dto.getGrossSale()-(voidsAndRefunds+dto.getTotalDiscount())),2));
            dto.setNetTotal(Formatter.round((dto.getGrossTotal()-(dto.getTax()+dto.getServiceCharge())),2));
        });

        double sumPayment = dataForSaleSummaryReportPaymentModeDto.stream().mapToDouble(val -> val.getNetTotal()).sum();
        dataForSaleSummaryReportPaymentModeDto.forEach(dto2 ->{dto2.setSale(Formatter.round(((dto2.getNetTotal()/sumPayment)*100),2));});

        int totalQuantityPaymentMode = dataForSaleSummaryReportPaymentModeDto.stream().mapToInt(i-> i.getQuantity()).sum();
        double totalGrossSalePaymentMode = Formatter.round(dataForSaleSummaryReportPaymentModeDto.stream().mapToDouble(i-> i.getGrossSale()).sum(),2);
        double totalDiscountPaymentMode =   Formatter.round(dataForSaleSummaryReportPaymentModeDto.stream().mapToDouble(i-> i.getTotalDiscount()).sum(),2);
        double grossTotalPaymentMode=  Formatter.round(dataForSaleSummaryReportPaymentModeDto.stream().mapToDouble(i-> i.getGrossTotal()).sum(),2);
        double totalTaxPaymentMode =  Formatter.round(dataForSaleSummaryReportPaymentModeDto.stream().mapToDouble(i-> i.getTax()).sum(),2);
        double totalServiceChargePaymentMode =   Formatter.round(dataForSaleSummaryReportPaymentModeDto.stream().mapToDouble(i-> i.getServiceCharge()).sum(),2);
        double totalNetTotalPaymentMode =  Formatter.round(dataForSaleSummaryReportPaymentModeDto.stream().mapToDouble(i-> i.getNetTotal()).sum(),2);
        double totalSalePaymentMode =  Formatter.round(dataForSaleSummaryReportPaymentModeDto.stream().mapToDouble(i-> i.getSale()).sum(),2);


        //refunds voids discounts
        List<VoidRefundSalesSummaryReport> voidsForSaleSummaryReport = receiptRepository.findVoidsForSaleSummaryReport(from, to, company.getId());
        List<VoidRefundSalesSummaryReport> refundsForSaleSummaryReport = receiptRepository.findRefundsForSaleSummaryReport(from, to, company.getId());
        List<VoidRefundSalesSummaryReport> discountsForSaleSummaryReport = receiptRepository.findDiscountsForSaleSummaryReport(from, to, company.getId());

        List<AmountDto> amountDtoListVoids = modelMapper.map(voidsForSaleSummaryReport, AmountDto.class);
        List<AmountDto> amountDtoListRefunds = modelMapper.map(refundsForSaleSummaryReport, AmountDto.class);
        List<AmountDto> amountDtoListDiscounts = modelMapper.map(discountsForSaleSummaryReport, AmountDto.class);

        double totalDiscounts = Formatter.round(amountDtoListDiscounts.stream().mapToDouble(i-> i.getAmount()).sum(),2);
        double totalVoids = Formatter.round(amountDtoListVoids.stream().mapToDouble(i-> i.getAmount()).sum(),2);
        double totalRefunds = Formatter.round(amountDtoListRefunds.stream().mapToDouble(i-> i.getAmount()).sum(),2);

        DailySalesSummeryReportResponse response = new DailySalesSummeryReportResponse();

        response.setFrom(from);
        response.setTo(to);
        response.setCompany(company.getName());


        response.setDataForSaleSummaryReportDiningTypeDto(dataForSaleSummaryReportDiningTypeDto);
        response.setDataForSaleSummaryReportCategoryDto(dataForSaleSummaryReportCategoryDto);
        response.setDataForSaleSummaryReportPaymentModeDto(dataForSaleSummaryReportPaymentModeDto);

        /*context.setVariable("dataForSaleSummaryReportDiningTypeDto", dataForSaleSummaryReportDiningTypeDto);
        context.setVariable("dataForSaleSummaryReportCategoryDto", dataForSaleSummaryReportCategoryDto);
        context.setVariable("dataForSaleSummaryReportPaymentModeDto", dataForSaleSummaryReportPaymentModeDto);*/

        response.setTotalQuantityDiningType(totalQuantityDiningType);
        response.setTotalGrossSaleDiningType(totalGrossSaleDiningType);
        response.setTotalDiscountDiningType(totalDiscountDiningType);
        response.setGrossTotalDiningType(grossTotalDiningType);
        response.setTotalTaxDiningType(totalTaxDiningType);
        response.setTotalServiceChargeDiningType(totalServiceChargeDiningType);
        response.setTotalNetTotalDiningType(totalNetTotalDiningType);
        response.setTotalSaleDiningType(totalSaleDiningType);

        /*context.setVariable("totalQuantityDiningTypeDto", totalQuantityDiningType);
        context.setVariable("totalGrossSaleDiningTypeDto", totalGrossSaleDiningType);
        context.setVariable("totalDiscountDiningTypeDto", totalDiscountDiningType);
        context.setVariable("grossTotalDiningTypeDto", grossTotalDiningType);
        context.setVariable("totalTaxDiningTypeDto", totalTaxDiningType);
        context.setVariable("totalServiceChargeDiningTypeDto", totalServiceChargeDiningType);
        context.setVariable("totalNetTotalDiningTypeDto", totalNetTotalDiningType);
        context.setVariable("totalSaleDiningTypeDto", totalSaleDiningType);*/

        response.setTotalQuantityCategory(totalQuantityCategory);
        response.setTotalGrossSaleCategory(totalGrossSaleCategory);
        response.setTotalDiscountCategory(totalDiscountCategory);
        response.setGrossTotalCategory(grossTotalCategory);
        response.setTotalTaxCategory(totalTaxCategory);
        response.setTotalServiceChargeCategory(totalServiceChargeCategory);
        response.setTotalNetTotalCategory(totalNetTotalCategory);
        response.setTotalSaleCategory(totalSaleCategory);

        /*context.setVariable("totalQuantityCategoryDto", totalQuantityCategory);
        context.setVariable("totalGrossSaleCategoryDto", totalGrossSaleCategory);
        context.setVariable("totalDiscountCategoryDto", totalDiscountCategory);
        context.setVariable("grossTotalCategoryDto", grossTotalCategory);
        context.setVariable("totalTaxCategoryDto", totalTaxCategory);
        context.setVariable("totalServiceChargeCategoryDto", totalServiceChargeCategory);
        context.setVariable("totalNetTotalCategoryDto", totalNetTotalCategory);
        context.setVariable("totalSaleCategoryDto", totalSaleCategory);*/

        response.setTotalQuantityPaymentMode(totalQuantityPaymentMode);
        response.setTotalGrossSalePaymentMode(totalGrossSalePaymentMode);
        response.setTotalDiscountPaymentMode(totalDiscountPaymentMode);
        response.setGrossTotalPaymentMode(grossTotalPaymentMode);
        response.setTotalTaxPaymentMode(totalTaxPaymentMode);
        response.setTotalServiceChargePaymentMode(totalServiceChargePaymentMode);
        response.setTotalNetTotalPaymentMode(totalNetTotalPaymentMode);
        response.setTotalSalePaymentMode(totalSalePaymentMode);

        /*context.setVariable("totalQuantityPaymentMode", totalQuantityPaymentMode);
        context.setVariable("totalGrossSalePaymentMode", totalGrossSalePaymentMode);
        context.setVariable("totalDiscountPaymentMode", totalDiscountPaymentMode);
        context.setVariable("grossTotalPaymentMode", grossTotalPaymentMode);
        context.setVariable("totalTaxPaymentMode", totalTaxPaymentMode);
        context.setVariable("totalServiceChargePaymentMode", totalServiceChargePaymentMode);
        context.setVariable("totalNetTotalPaymentMode", totalNetTotalPaymentMode);
        context.setVariable("totalSalePaymentMode", totalSalePaymentMode);*/

        response.setVoids(amountDtoListVoids);
        response.setRefunds(amountDtoListRefunds);
        response.setDiscounts(amountDtoListDiscounts);

        /*context.setVariable("voids", amountDtoListVoids);
        context.setVariable("refunds", amountDtoListRefunds);
        context.setVariable("discounts", amountDtoListDiscounts);*/

        response.setTotalDiscounts(totalDiscounts);
        response.setTotalVoids(totalVoids);
        response.setTotalRefunds(totalRefunds);

        /*context.setVariable("totalDiscounts", totalDiscounts);
        context.setVariable("totalVoids", totalVoids);
        context.setVariable("totalRefunds", totalRefunds);*/

        return response;
        //return templateEngine.process("sale_summary_report_template", context);

    }


    @Override
    public DailySalesMixReportResponseDto parseThymeleafTemplateForDailySaleMixReport(Company company, String from, String to) {
        List<DailySalesMixReportDto> dailySalesMixReportDtoList = getStringCategoryDtoMap(from, to, company.getId());

        Map<String, List<DailySalesMixReportDto>> map = dailySalesMixReportDtoList.stream()
          .filter(DailySalesMixReportDto -> DailySalesMixReportDto.getCategoryName() != null)
          .collect(groupingBy(DailySalesMixReportDto::getCategoryName));

        /*Map<String, Map<String, List<DailySalesMixReportDto>>> map = dailySalesMixReportDtoList.stream()
          .filter(DailySalesMixReportDto -> DailySalesMixReportDto.getCategoryName() != null)
          .filter(DailySalesMixReportDto -> DailySalesMixReportDto.getItemName() != null)
          .collect(groupingBy(DailySalesMixReportDto::getCategoryName, groupingBy(DailySalesMixReportDto::getItemName)));*/

        Iterator<Map.Entry<String, List<DailySalesMixReportDto>>> categoryMap = map.entrySet().iterator();

        List<DailySalesMixReportResponse> dailySalesMixReportResponseList = new ArrayList<>();

        log.info("map size : {}", map.size());

        while (categoryMap.hasNext()) {

            DailySalesMixReportResponse dailySalesMixReportResponse = new DailySalesMixReportResponse();

            Map.Entry<String, List<DailySalesMixReportDto>> categoryPair = categoryMap.next();

            log.info("category key : {}", categoryPair.getKey());
            dailySalesMixReportResponse.setCategoryName(categoryPair.getKey());

            List<DailySalesMixReportDto> itemDescList = categoryPair.getValue();

            Map<String, List<DailySalesMixReportDto>> itemDescMap =
              itemDescList.stream()
                .filter(DailySalesMixReportDto -> DailySalesMixReportDto.getItemName() != null)
                .collect(groupingBy(DailySalesMixReportDto::getItemName));

            Iterator<Map.Entry<String, List<DailySalesMixReportDto>>> itemDescriptionMap = itemDescMap.entrySet().iterator();

            while (itemDescriptionMap.hasNext()) {
                Map.Entry<String, List<DailySalesMixReportDto>> itemDescriptionGroup = itemDescriptionMap.next();

                log.info("itemDescription key : {}", itemDescriptionGroup.getKey());
                dailySalesMixReportResponse.setItemDescription(itemDescriptionGroup.getKey());

                List<DailySalesMixReportDto> DailySalesMixReportVariantList = itemDescriptionGroup.getValue();

                Integer itemSold = DailySalesMixReportVariantList.stream().collect(Collectors.summingInt(DailySalesMixReportDto::getQuantity));
                dailySalesMixReportResponse.setItemSold(itemSold);

                Double grossSale = DailySalesMixReportVariantList.stream().collect(summingDouble(DailySalesMixReportDto::getGrossTotalMoney));
                dailySalesMixReportResponse.setGrossSale(grossSale);


                Map<String, List<DailySalesMixReportDto>> variantMap =
                  DailySalesMixReportVariantList.stream()
                    .filter(DailySalesMixReportDto -> DailySalesMixReportDto.getVariantName() != null)
                    .collect(groupingBy(DailySalesMixReportDto::getVariantName));

                dailySalesMixReportResponse.setVariantMap(variantMap);

                Iterator<Map.Entry<String, List<DailySalesMixReportDto>>> variant = variantMap.entrySet().iterator();

                List<DailySalesByVariantResponse> dailySalesByVariantResponses = new ArrayList<>();

                while (variant.hasNext()) {

                    Map.Entry<String, List<DailySalesMixReportDto>> variantVal = variant.next();

                    log.info("itemDescription key : {}", variantVal.getKey());

                    List<DailySalesMixReportDto> variantList = variantVal.getValue();

                    Integer itemSoldVariant =
                      variantList.stream().collect(Collectors.summingInt(DailySalesMixReportDto::getQuantity));


                    Double grossSaleVariant = variantList.stream().collect(summingDouble(DailySalesMixReportDto::getGrossTotalMoney));

                    DailySalesByVariantResponse dailySalesByVariantResponse = new DailySalesByVariantResponse();
                    dailySalesByVariantResponse.setVariantName(variantVal.getKey());
                    dailySalesByVariantResponse.setItemSold(itemSoldVariant);
                    dailySalesByVariantResponse.setGrossSale(grossSaleVariant);

                    dailySalesByVariantResponses.add(dailySalesByVariantResponse);

                }
                dailySalesMixReportResponse.setDailySalesByVariantResponses(dailySalesByVariantResponses);


                dailySalesMixReportResponseList.add(dailySalesMixReportResponse);




                /*while (itemDesc.hasNext()) {

                    Map.Entry<String, List<DailySalesMixReportDto>> itemDescPair = itemDesc.next();

                    dailySalesMixReportResponse.setItemDescription(itemDescPair.getKey());

                    List<DailySalesMixReportDto> list = itemDescPair.getValue();

                    Integer itemSold = list.stream().collect(Collectors.summingInt(DailySalesMixReportDto::getQuantity));
                    dailySalesMixReportResponse.setItemSold(itemSold);

                    Double grossSale = list.stream().collect(summingDouble(DailySalesMixReportDto::getGrossTotalMoney));
                    dailySalesMixReportResponse.setGrossSale(grossSale);



                    Map<String, Double> grossSaleByVariant = list.stream()
                      .filter(DailySalesMixReportDto -> DailySalesMixReportDto.getVariantName() != null)
                      .collect(groupingBy(DailySalesMixReportDto::getVariantName,
                        summingDouble(DailySalesMixReportDto::getGrossTotalMoney)));

                    DailySalesByVariantResponse dailySalesByVariantResponse = new DailySalesByVariantResponse();

                    dailySalesMixReportResponse.setItemSoldByVariant(itemSoldByVariant);

                    dailySalesMixReportResponseList.add(dailySalesMixReportResponse);




                }*/

            }
            /*dailySalesMixReportResponse.setCategoryName(CategoryPair.getKey());
            Iterator<Map.Entry<String, List<DailySalesMixReportDto>>> itemDesc =
              CategoryPair.getValue().entrySet().iterator();*/

            //List<DailySalesMixReportResponse.ItemData> items = new ArrayList<>();

        }

        DailySalesMixReportResponseDto reportResponseDto = new DailySalesMixReportResponseDto();
        reportResponseDto.setDailySalesMixReportMap(map);
        reportResponseDto.setDailySalesMixReportList(dailySalesMixReportDtoList);
        reportResponseDto.setDailySalesMixReportResponseList(dailySalesMixReportResponseList);
        reportResponseDto.setCompanyName(company.getName());
        reportResponseDto.setDate(LocalDate.now());

        /*TemplateEngine templateEngine = getTemplateEngine();
        Context context = new Context();*/
        /*context.setVariable("dailySalesMixReportMap", map);
        context.setVariable("dailySalesMixReportList", dailySalesMixReportDtoList);
        context.setVariable("dailySalesMixReportResponseList", dailySalesMixReportResponseList);
        context.setVariable("companyName", company.getName());
        context.setVariable("date", LocalDate.now());*/
        return reportResponseDto;
    }






    /*@Override
    public String parseThymeleafTemplateForHourlySaleReport(Company company ,String from , String to)  {
        List<HourlySaleReport> dataForHourlySaleReport = receiptRepository.findDataForHourlySaleReport(from, to, company.getId());

        List<HourlySaleReportDto> hourlySaleReportDto = modelMapper.map(dataForHourlySaleReport, HourlySaleReportDto.class);

        LinkedHashMap<Integer, HourlySaleReportDataDto> hourlySaleMap = new LinkedHashMap();
        LinkedHashMap<Integer, HourlySaleReportDataDto> hourlySaleReportMap = new LinkedHashMap();


        hourlySaleReportDto.forEach(hourlySale -> {

            switch (hourlySale.getCreatedAt().substring(11, 13)) {
                case "03":
                    extracted(hourlySaleMap, hourlySale, 3);
                    break;
                case "04":
                    extracted(hourlySaleMap, hourlySale, 4);
                case "05":
                    extracted(hourlySaleMap, hourlySale, 5);
                    break;
                case "06":
                    extracted(hourlySaleMap, hourlySale, 6);
                case "07":
                    extracted(hourlySaleMap, hourlySale, 7);
                    break;
                case "08":
                    extracted(hourlySaleMap, hourlySale, 8);
                case "09":
                    extracted(hourlySaleMap, hourlySale, 9);
                    break;
                case "10":
                    extracted(hourlySaleMap, hourlySale, 10);
                case "11":
                    extracted(hourlySaleMap, hourlySale, 11);
                    break;
                case "12":
                    extracted(hourlySaleMap, hourlySale, 12);
                case "13":
                    extracted(hourlySaleMap, hourlySale, 13);
                    break;
                case "14":
                    extracted(hourlySaleMap, hourlySale, 14);
                case "15":
                    extracted(hourlySaleMap, hourlySale, 15);
                case "16":
                    extracted(hourlySaleMap, hourlySale, 15);
                    break;
                case "17":
                    extracted(hourlySaleMap, hourlySale, 17);
                case "18":
                    extracted(hourlySaleMap, hourlySale, 18);
                    break;
                case "19":
                    extracted(hourlySaleMap, hourlySale, 19);
                case "20":
                    extracted(hourlySaleMap, hourlySale, 20);
                case "21":
                    extracted(hourlySaleMap, hourlySale, 21);
                    break;
                case "22":
                    extracted(hourlySaleMap, hourlySale, 22);
                case "23":
                    extracted(hourlySaleMap, hourlySale, 23);
                case "24":
                    extracted(hourlySaleMap, hourlySale, 24);
                    break;
                case "01":
                    extracted(hourlySaleMap, hourlySale, 1);
                case "02":
                    extracted(hourlySaleMap, hourlySale, 2);
            }

        });

        double saleSum = hourlySaleMap.values().stream().mapToDouble(d -> d.getSale()).sum();
        double avgSaleSum = hourlySaleMap.values().stream().mapToDouble(d -> d.getAvgSale()).sum();
        int itemSum = hourlySaleMap.values().stream().mapToInt(d -> d.getItems()).sum();
        int transactionsSum = hourlySaleMap.values().stream().mapToInt(d -> d.getTransactions()).sum();



        for(int i=0 ; i<=25 ; i++){
            if(hourlySaleMap.get(i)==null){
                HourlySaleReportDataDto  hourlySaleReportDataDto =  new HourlySaleReportDataDto();
                hourlySaleReportMap.put(i,hourlySaleReportDataDto);
            }else{
                hourlySaleMap.get(i).setPercentageSale(Formatter.round((hourlySaleMap.get(i).getSale()/saleSum)*100,2));
                hourlySaleReportMap.put(i,hourlySaleMap.get(i));
            }
        }


        TemplateEngine templateEngine = getTemplateEngine();
        Context context = new Context();
        context.setVariable("hourlySaleMap", hourlySaleReportMap);
        context.setVariable("itemSum", itemSum);
        context.setVariable("saleSum", saleSum);
        context.setVariable("avgSaleSum", avgSaleSum);
        context.setVariable("transactionsSum", transactionsSum);
        context.setVariable("date", LocalDate.now());

        return templateEngine.process("hourly_sale_report_template", context);


    }

    @Override
    public String parseThymeleafTemplateForDiscountDetailReport(Company company, String from, String to) {
        List<DiscountDetailReport> discountDetailReport = receiptRepository.findDiscountDetailReport(from, to, company.getId());
        List<DiscountDetailReportDto> discountDetailReportDtos = modelMapper.map(discountDetailReport, DiscountDetailReportDto.class);

        double totalSales=0;
        double totalDiscount=0;
        double totalTax=0;
        double totalServiceCharge=0;
        double totalTotal=0;

        discountDetailReportDtos.forEach(discountDetailReportDto -> {
            discountDetailReportDto.setOrderTime(discountDetailReportDto.getOrderTime().substring(11,16));
            discountDetailReportDto.setTotal(discountDetailReportDto.getSales()-(discountDetailReportDto.getDiscount()+discountDetailReportDto.getTax()));
        });

        for (DiscountDetailReportDto discountDetailReportDto:discountDetailReportDtos) {
            discountDetailReportDto.setCategory(discountDetailReportDto.getCategory().trim());
            totalSales += discountDetailReportDto.getSales();
            totalDiscount += discountDetailReportDto.getDiscount();
            totalTax += discountDetailReportDto.getTax();
            totalServiceCharge += discountDetailReportDto.getServiceCharge();
            totalTotal += discountDetailReportDto.getTotal();
        }



        TemplateEngine templateEngine = getTemplateEngine();
        Context context = new Context();
        context.setVariable("discountDetailReportDtos", discountDetailReportDtos);
        context.setVariable("companyName", company.getName());
        context.setVariable("date", LocalDate.now());

        context.setVariable("totalSales", totalSales);
        context.setVariable("totalDiscount", totalDiscount);
        context.setVariable("totalTax", totalTax);
        context.setVariable("totalServiceCharge", totalServiceCharge);
        context.setVariable("totalTotal", totalTotal);


        return templateEngine.process("discount detail_report_template", context);

    }

    @Override
    public String parseThymeleafTemplateForConsolidatedDailySalesSummaryReport(Company company, String from, String to) {

        List<ConsolidatedDailySalesSummaryReport> summaryReportDiningOptions = receiptRepository.findConsolidatedDailySalesSummaryReportDiningOptions(from, to, company.getId());

        List<ConsolidatedDailySalesSummaryReport> summaryReportDelivery = receiptRepository.findConsolidatedDailySalesSummaryReportDelivery(from, to, company.getId());

        List<ConsolidatedSalesSummaryReportDto> consolidatedSalesSummaryReportDtoDining = modelMapper.map(summaryReportDiningOptions, ConsolidatedSalesSummaryReportDto.class);
        List<ConsolidatedSalesSummaryReportDto> consolidatedSalesSummaryReportDtoDelivery = modelMapper.map(summaryReportDelivery, ConsolidatedSalesSummaryReportDto.class);

        List<ConsolidatedSalesSummaryReportDto> reportList = new ArrayList<>();
        reportList.addAll(consolidatedSalesSummaryReportDtoDining);
        reportList.addAll(consolidatedSalesSummaryReportDtoDelivery);

        Map <String,List<ConsolidatedSummaryData>> consolidatedSummaryDataMap = new LinkedHashMap();

        reportList.forEach(consolidatedSalesSummaryReportDto -> {
           String store="";
            log.info("store => {}", consolidatedSalesSummaryReportDto.getStore());
            if("Island Wraps - Dine In".equals(consolidatedSalesSummaryReportDto.getStore()) ||
              "Island Wraps - Delivery".equals(consolidatedSalesSummaryReportDto.getStore())) {
                String[] storeArray = consolidatedSalesSummaryReportDto.getStore().split("-");
                store = storeArray[0].trim();
               log.info("store {}",storeArray[0].trim());
            } else {
                String[] storeArray = consolidatedSalesSummaryReportDto.getStore().split(":");
                store = storeArray[0].trim();
                log.info("store {}",storeArray[0].trim());
            }
            if(consolidatedSummaryDataMap.get(store)==null) {
                List<ConsolidatedSummaryData> consolidatedSummaryDataList =  new ArrayList<>();
                ConsolidatedSummaryData consolidatedSummaryData =  new ConsolidatedSummaryData();
                consolidatedSummaryData.setOption(consolidatedSalesSummaryReportDto.getDiningOption()!=null?consolidatedSalesSummaryReportDto.getDiningOption():consolidatedSalesSummaryReportDto.getPayment());
                consolidatedSummaryData.setValue(consolidatedSalesSummaryReportDto.getTotalMoney());
                consolidatedSummaryData.setCount(consolidatedSalesSummaryReportDto.getCount());
                consolidatedSummaryDataList.add(consolidatedSummaryData);
                consolidatedSummaryDataMap.put(store, consolidatedSummaryDataList);
            } else {
                ConsolidatedSummaryData consolidatedSummaryData =  new ConsolidatedSummaryData();
                consolidatedSummaryData.setOption(consolidatedSalesSummaryReportDto.getDiningOption()!=null?consolidatedSalesSummaryReportDto.getDiningOption():consolidatedSalesSummaryReportDto.getPayment());
                consolidatedSummaryData.setValue(consolidatedSalesSummaryReportDto.getTotalMoney());
                consolidatedSummaryData.setCount(consolidatedSalesSummaryReportDto.getCount());
                consolidatedSummaryDataMap.get(store).add(consolidatedSummaryData);

            }

        });

        List<ConsolidatedDailySummaryResponse.ConsolidatedDailySummaryData> list = new ArrayList<>();
        ConsolidatedDailySummaryResponse.ConsolidatedDailySummaryData consolidatedDailySummaryResponse = null;
        ConsolidatedDailySummaryResponse response = new ConsolidatedDailySummaryResponse();

        double fullTotal=0.0;
        int fullTotalCount = 0;
        double total=0.0;
        Integer totalCount=0;
        for (Map.Entry<String, List<ConsolidatedSummaryData>> entry : consolidatedSummaryDataMap.entrySet()) {

            System.out.println(entry.getKey() + "/" + entry.getValue());
            String[] storeArray = entry.getKey().split("-");
            System.out.println(storeArray[0]);

            if(list.isEmpty()) {
                consolidatedDailySummaryResponse =
                  new ConsolidatedDailySummaryResponse.ConsolidatedDailySummaryData(storeArray[0].trim());
            } else {
                Optional<ConsolidatedDailySummaryResponse.ConsolidatedDailySummaryData> consolidatedDailySummaryResponseOptional =
                  list.stream().filter(record -> record.getStore().equals(storeArray[0].trim())).findFirst();
                if(consolidatedDailySummaryResponseOptional.isPresent()) {
                    consolidatedDailySummaryResponse =
                      consolidatedDailySummaryResponseOptional.get();
                    list.remove(consolidatedDailySummaryResponse);
                } else {
                    consolidatedDailySummaryResponse =
                      new ConsolidatedDailySummaryResponse.ConsolidatedDailySummaryData(storeArray[0]);
                     total=0.0;
                     totalCount=0;
                }
            }
            
            for (ConsolidatedSummaryData consolidatedSalesSummaryReportDto : entry.getValue()) {

                if(consolidatedSalesSummaryReportDto.getOption() != null && consolidatedSalesSummaryReportDto.getOption().equals("Dine-in")) {
                    consolidatedDailySummaryResponse.setDineIn(consolidatedSalesSummaryReportDto.getValue());
                    consolidatedDailySummaryResponse.setDineInCount(consolidatedSalesSummaryReportDto.getCount());
                    total += consolidatedSalesSummaryReportDto.getValue();
                    totalCount += consolidatedSalesSummaryReportDto.getCount();
                } else if( consolidatedSalesSummaryReportDto.getOption() != null && consolidatedSalesSummaryReportDto.getOption().equals("Takeaway")) {
                    consolidatedDailySummaryResponse.setTakeAway(consolidatedSalesSummaryReportDto.getValue());
                    consolidatedDailySummaryResponse.setTakeAwayCount(consolidatedSalesSummaryReportDto.getCount());
                    total += consolidatedSalesSummaryReportDto.getValue();
                    totalCount += consolidatedSalesSummaryReportDto.getCount();
                } else if(consolidatedSalesSummaryReportDto.getOption().equals("PickMe")) {
                    consolidatedDailySummaryResponse.setPickMe(consolidatedSalesSummaryReportDto.getValue());
                    consolidatedDailySummaryResponse.setPickMeCount(consolidatedSalesSummaryReportDto.getCount());
                    total += consolidatedSalesSummaryReportDto.getValue();
                    totalCount += consolidatedSalesSummaryReportDto.getCount();
                } else if(consolidatedSalesSummaryReportDto.getOption().equals("UberEats")) {
                    consolidatedDailySummaryResponse.setUberEats(consolidatedSalesSummaryReportDto.getValue());
                    consolidatedDailySummaryResponse.setUberEatsCount(consolidatedSalesSummaryReportDto.getCount());
                    total += consolidatedSalesSummaryReportDto.getValue();
                    totalCount += consolidatedSalesSummaryReportDto.getCount();
                }
            }
            consolidatedDailySummaryResponse.setTotal(total);
            consolidatedDailySummaryResponse.setTotalCount(totalCount);
            fullTotal += total;
            fullTotalCount += totalCount;
            list.add(consolidatedDailySummaryResponse);
        }
        response.setFullTotal(fullTotal);
        response.setFullTotalCount(fullTotalCount);
        response.setConsolidatedSummaryList(list);

        TemplateEngine templateEngine = getTemplateEngine();
        Context context = new Context();
        context.setVariable("consolidatedSummaryResponse", response);
        context.setVariable("companyName", company.getName());
        context.setVariable("date", LocalDate.now());
        return templateEngine.process("consolidated_daily_sales_summary_report_template", context);

    }

    @Override
    public String parseThymeleafTemplateForSettlementModeWiseReport(Company company, String from, String to) {
        List<SettlementModeWiseReport> settlementModeWiseReports = receiptRepository.findDataForSettlementModeViewReport(from, to, company.getId());

        List<SettlementModeWiseReportDtoData.ReportData> settlementModeWiseReportDto = modelMapper.map(settlementModeWiseReports, SettlementModeWiseReportDtoData.ReportData.class);

        LinkedHashMap<String, SettlementModeWiseReportDtoData> settlementModeWiseReportDtoLinkedHashMap = new LinkedHashMap();

        settlementModeWiseReportDto.forEach(settlementModeWiseReport -> {

            SettlementModeWiseReportDtoData settlementModeWiseReportDtoData = settlementModeWiseReportDtoLinkedHashMap.get(settlementModeWiseReport.getSettlement());

            if (settlementModeWiseReportDtoData == null) {
                SettlementModeWiseReportDtoData.ReportData reportData = new SettlementModeWiseReportDtoData.ReportData();
                SettlementModeWiseReportDtoData dtoData = new SettlementModeWiseReportDtoData();
                ArrayList<SettlementModeWiseReportDtoData.ReportData> settlementModeWiseReportDtoArrayList = new ArrayList();

                reportData.setSettlement(settlementModeWiseReport.getSettlement());
                reportData.setOrderNo(settlementModeWiseReport.getOrderNo());
                reportData.setOrderType(settlementModeWiseReport.getOrderType());
                try {
                    reportData.setCreatedAt(Formatter.convertToNewFormat(settlementModeWiseReport.getCreatedAt()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                reportData.setUserName(settlementModeWiseReport.getUserName());
                reportData.setSale(settlementModeWiseReport.getSale());
                reportData.setRemark(settlementModeWiseReport.getRemark());

                dtoData.setSubTotal(Formatter.round(dtoData.getSubTotal() + reportData.getSale(), 2));
                settlementModeWiseReportDtoArrayList.add(reportData);
                dtoData.setReportDataList(settlementModeWiseReportDtoArrayList);

                settlementModeWiseReportDtoLinkedHashMap.put(reportData.getSettlement(), dtoData);
            } else {

                SettlementModeWiseReportDtoData.ReportData reportData = new SettlementModeWiseReportDtoData.ReportData();
                reportData.setSettlement(settlementModeWiseReport.getSettlement());
                reportData.setOrderNo(settlementModeWiseReport.getOrderNo());
                reportData.setOrderType(settlementModeWiseReport.getOrderType());
                try {
                    reportData.setCreatedAt(Formatter.convertToNewFormat(settlementModeWiseReport.getCreatedAt()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                reportData.setUserName(settlementModeWiseReport.getUserName());
                reportData.setSale(settlementModeWiseReport.getSale());
                reportData.setRemark(settlementModeWiseReport.getRemark());

                settlementModeWiseReportDtoData.setSubTotal(Formatter.round(settlementModeWiseReportDtoData.getSubTotal() + reportData.getSale(), 2));
                settlementModeWiseReportDtoData.getReportDataList().add(reportData);

            }

        });

        TemplateEngine templateEngine = getTemplateEngine();
        Context context = new Context();
        context.setVariable("settlementModeWiseReportDtoLinkedHashMap", settlementModeWiseReportDtoLinkedHashMap);
        context.setVariable("companyName", company.getName());
        context.setVariable("date", LocalDate.now());
        return templateEngine.process("settlement_mode_wise_report_template", context);

    }


    @Override
    public String parseThymeleafTemplateForVoidRefundDetailReport() {

        List<VoidRefundDetailReport> voidRefundDetailReports = receiptRepository.findDataForVoidRefundDetailForm(null, null, null);

        List<VoidRefundDetailReportDto> voidRefundDetailReportDto = modelMapper.map(voidRefundDetailReports, VoidRefundDetailReportDto.class);

        LinkedHashMap<String, List<VoidRefundDetailReportDto>> voidRefundDetailReportDtoLinkedHashMap = new LinkedHashMap();

        voidRefundDetailReportDto.forEach(voidRefundDetailReport -> {
            List<VoidRefundDetailReportDto> voidRefundDetailReportDtos = voidRefundDetailReportDtoLinkedHashMap.get(voidRefundDetailReport.getReceiptType());

            if (voidRefundDetailReportDtos == null) {
                VoidRefundDetailReportDto voidRefundDetailReportDto1 = new VoidRefundDetailReportDto();
                ArrayList<VoidRefundDetailReportDto> voidRefundDetailReportDtoArrayList = new ArrayList();

                voidRefundDetailReportDto1.setType(voidRefundDetailReport.getType());
                voidRefundDetailReportDto1.setReceiptType(voidRefundDetailReport.getReceiptType());
                voidRefundDetailReportDto1.setReason(voidRefundDetailReport.getReason());
                voidRefundDetailReportDto1.setOrderNo(voidRefundDetailReport.getOrderNo());
                voidRefundDetailReportDto1.setCreatedAt(voidRefundDetailReport.getCreatedAt().substring(11,16));
                voidRefundDetailReportDto1.setUserName(voidRefundDetailReport.getUserName());
                voidRefundDetailReportDto1.setOrderType(voidRefundDetailReport.getOrderType());
                voidRefundDetailReportDto1.setSale(voidRefundDetailReport.getSale());
                voidRefundDetailReportDto1.setAuthorizedId(voidRefundDetailReport.getAuthorizedId());

                voidRefundDetailReportDtoArrayList.add(voidRefundDetailReportDto1);
                voidRefundDetailReportDtoLinkedHashMap.put(voidRefundDetailReportDto1.getReceiptType(), voidRefundDetailReportDtoArrayList);
            } else {
                VoidRefundDetailReportDto voidRefundDetailReportDto1 = new VoidRefundDetailReportDto();

                voidRefundDetailReportDto1.setType(voidRefundDetailReport.getType());
                voidRefundDetailReportDto1.setReceiptType(voidRefundDetailReport.getReceiptType());
                voidRefundDetailReportDto1.setReason(voidRefundDetailReport.getReason());
                voidRefundDetailReportDto1.setOrderNo(voidRefundDetailReport.getOrderNo());
                voidRefundDetailReportDto1.setCreatedAt(voidRefundDetailReport.getCreatedAt().substring(11,16));
                voidRefundDetailReportDto1.setUserName(voidRefundDetailReport.getUserName());
                voidRefundDetailReportDto1.setOrderType(voidRefundDetailReport.getOrderType());
                voidRefundDetailReportDto1.setSale(voidRefundDetailReport.getSale());
                voidRefundDetailReportDto1.setAuthorizedId(voidRefundDetailReport.getAuthorizedId());

                voidRefundDetailReportDtos.add(voidRefundDetailReportDto1);
            }

        });

        TemplateEngine templateEngine = getTemplateEngine();
        Context context = new Context();
        context.setVariable("voidRefundDetailReportDtoLinkedHashMap", voidRefundDetailReportDtoLinkedHashMap);
        return templateEngine.process("void_refund_detail_report_template", context);

    }

    private void extracted(LinkedHashMap<Integer, HourlySaleReportDataDto> hourlySaleMap, HourlySaleReportDto hourlySale, int no) {
        HourlySaleReportDataDto hourlySaleReportDataDto = hourlySaleMap.get(no);
        if (hourlySaleReportDataDto == null) {
            HourlySaleReportDataDto newHourlySaleReportDataDto = new HourlySaleReportDataDto();
            newHourlySaleReportDataDto.setSale(round(hourlySale.getTotalMoney(), 2));
            newHourlySaleReportDataDto.setItems(hourlySale.getLineItemId());
            newHourlySaleReportDataDto.setTransactions(1);
            hourlySaleMap.put(no, newHourlySaleReportDataDto);
        } else {
            hourlySaleReportDataDto.setSale(hourlySaleReportDataDto.getSale() + hourlySale.getTotalMoney());
            hourlySaleReportDataDto.setTransactions(hourlySaleReportDataDto.getTransactions() + 1);
            hourlySaleReportDataDto.setSale(round(hourlySaleReportDataDto.getSale(), 2));
            hourlySaleReportDataDto.setAvgSale(round(hourlySaleReportDataDto.getSale() / hourlySaleReportDataDto.getTransactions(), 2));
        }
    }

    private TemplateEngine getTemplateEngine() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }



    private List<DailySalesMixReportDto> getStringCategoryDtoMap(String from, String to, Long companyId) {
        List<DailySalesMixReport> dataForDailySaleMixReport = receiptRepository.findDataForDailySaleMixReport(from, to, companyId);
        List<DailySalesMixReportDto> dailySalesMixReportDtoList = modelMapper.map(dataForDailySaleMixReport, DailySalesMixReportDto.class);


        return dailySalesMixReportDtoList;
    }

    *//*private Map<String, CategoryDto> getStringCategoryDtoMap(String from, String to, Long companyId) {
        List<DailySalesMixReport> dataForDailySaleMixReport = receiptRepository.findDataForDailySaleMixReport(from, to, companyId);
        List<DailySalesMixReportDto> dailySalesMixReportDtoList = modelMapper.map(dataForDailySaleMixReport, DailySalesMixReportDto.class);

        Map<String, CategoryDto> categoryDtoMap = new HashMap<>();

        dailySalesMixReportDtoList.forEach(dailySalesMixReportDto -> {

            //category

            //get cat dto
            CategoryDto categoryDto1 = categoryDtoMap.get(dailySalesMixReportDto.getCategoryId());

            //create new cat dto
            if (categoryDto1 == null) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setCategoryId(dailySalesMixReportDto.getCategoryId());
                categoryDto.setCategory(dailySalesMixReportDto.getCategory());
                categoryDtoMap.put(categoryDto.getCategoryId(), categoryDto);
            }

            // item

            //item dto map
            if (categoryDtoMap.get(dailySalesMixReportDto.getCategoryId()).getItemDtoMap() == null) {
                Map<String, ItemDto> itemDtoMap = new HashMap<>();
                categoryDtoMap.get(dailySalesMixReportDto.getCategoryId()).setItemDtoMap(itemDtoMap);
            }

            ItemDto itemDto1 = categoryDtoMap.get(dailySalesMixReportDto.getCategoryId()).getItemDtoMap().get(dailySalesMixReportDto.getItemId());

            if (itemDto1 == null) {
                ItemDto itemDto = new ItemDto();
                itemDto.setItemId(dailySalesMixReportDto.getItemId());
                itemDto.setItemName(dailySalesMixReportDto.getItemName());
                itemDto.setGrossSale(dailySalesMixReportDto.getGrossTotalMoney());
                categoryDtoMap.get(dailySalesMixReportDto.getCategoryId()).getItemDtoMap().put(dailySalesMixReportDto.getItemId(), itemDto);
            } else {
                itemDto1.setItemSoldCount(itemDto1.getItemSoldCount() == null ? 0 : itemDto1.getItemSoldCount() + 1);
                itemDto1.setGrossSale(itemDto1.getGrossSale() + dailySalesMixReportDto.getGrossTotalMoney());
            }

            //itemType
            if (categoryDtoMap.get(dailySalesMixReportDto.getCategoryId()).getItemDtoMap().get(dailySalesMixReportDto.getItemId()).getItemTypeDtoMap() == null) {
                Map<String, ItemTypeDto> itemTypeDtoMap = new HashMap<>();
                categoryDtoMap.get(dailySalesMixReportDto.getCategoryId()).getItemDtoMap().get(dailySalesMixReportDto.getItemId()).setItemTypeDtoMap(itemTypeDtoMap);
            }

            ItemTypeDto itemTypeDto = categoryDtoMap.get(dailySalesMixReportDto.getCategoryId()).getItemDtoMap().get(dailySalesMixReportDto.getItemId()).getItemTypeDtoMap().get(dailySalesMixReportDto.getVariantName());

            if (itemTypeDto == null) {
                ItemTypeDto itemTypeDto1 = new ItemTypeDto();
                itemTypeDto1.setVariantName(dailySalesMixReportDto.getVariantName() == null ? "-" : dailySalesMixReportDto.getVariantName());
                if (dailySalesMixReportDto.getReceiptType().equalsIgnoreCase("REFUND")) {
                    itemTypeDto1.setGrossSale(round((itemTypeDto1.getGrossSale() - dailySalesMixReportDto.getGrossTotalMoney()), 2));
                    itemTypeDto1.setRefundedAmount(itemTypeDto1.getRefundedAmount() + dailySalesMixReportDto.getGrossTotalMoney());
                    itemTypeDto1.setRefundedCount(itemTypeDto1.getRefundedCount() + 1);
                } else {
                    itemTypeDto1.setGrossSale(round(dailySalesMixReportDto.getGrossTotalMoney(), 2));
                }
                itemTypeDto1.setItemSoldCount(1);
                categoryDtoMap.get(dailySalesMixReportDto.getCategoryId()).getItemDtoMap().get(dailySalesMixReportDto.getItemId()).getItemTypeDtoMap().put(dailySalesMixReportDto.getVariantName() == null ? "-" : dailySalesMixReportDto.getVariantName(), itemTypeDto1);
            } else {
                if (dailySalesMixReportDto.getReceiptType().equalsIgnoreCase("REFUND")) {
                    itemTypeDto.setGrossSale(round(itemTypeDto.getGrossSale() - dailySalesMixReportDto.getGrossTotalMoney(), 2));
                    itemTypeDto.setRefundedAmount(itemTypeDto.getRefundedAmount() + dailySalesMixReportDto.getGrossTotalMoney());
                    itemTypeDto.setRefundedCount(itemTypeDto.getRefundedCount() + 1);
                }
                itemTypeDto.setGrossSale(round(itemTypeDto.getGrossSale() + dailySalesMixReportDto.getGrossTotalMoney(), 2));
                itemTypeDto.setItemSoldCount(itemTypeDto.getItemSoldCount() + 1);
            }

        });


        categoryDtoMap.forEach((s, categoryDto) -> {
            categoryDto.getItemDtoMap().forEach((s1, itemDto) -> {
                itemDto.setItemSoldCount(itemDto.getItemTypeDtoMap().size());
                itemDto.getItemTypeDtoMap().forEach((s2, itemTypeDto) -> {
                    itemTypeDto.setNetTotalSale(round(itemTypeDto.getGrossSale() - itemTypeDto.getRefundedAmount(), 2));
                });
            });

        });
        return categoryDtoMap;
    }*//*

    public double getSum() {
        return 12.5;
    }*/

    @Override
    public List<DailySalesMixReportDto> getStringCategoryDtoMap(String from, String to, Long companyId) {
        List<DailySalesMixReport> dataForDailySaleMixReport = receiptRepository.findDataForDailySaleMixReport(from, to, companyId);
        List<DailySalesMixReportDto> dailySalesMixReportDtoList = modelMapper.map(dataForDailySaleMixReport, DailySalesMixReportDto.class);
        return dailySalesMixReportDtoList;
    }

    private Map<String, List<OptionDto>> getModifierDtoMap(String from, String to, Long companyId) {

        List<DailySaleModifierReport> dataForDailySaleModifier = receiptRepository.findDataForDailySaleModifier(from, to, companyId);

        List<DailySalesModifierReportDto> dailySalesModifierReportDtoList = modelMapper.map(dataForDailySaleModifier, DailySalesModifierReportDto.class);

        Map<String, List<OptionDto>> modifierDtoMap = new HashMap<>();

        dailySalesModifierReportDtoList.forEach(dailySalesModifierReportDto -> {

            List<OptionDto> optionDtoList =
              modifierDtoMap.get(dailySalesModifierReportDto.getCategory().trim());

            if (!Optional.ofNullable(optionDtoList).isPresent()) {

                OptionDto optionDto = new OptionDto();
                List<OptionDto> optionDtoListNew = new ArrayList<>();

                optionDto.setOptionName(dailySalesModifierReportDto.getOption());
                if (dailySalesModifierReportDto.getReceiptType().equalsIgnoreCase("SALE")) {
                    optionDto.setItemsSold(1);
                    optionDto.setGrossSale(dailySalesModifierReportDto.getPrice());
                }
                if (dailySalesModifierReportDto.getReceiptType().equalsIgnoreCase("REFUND")) {
                    optionDto.setItemRefunded(1);
                    optionDto.setRefunds(dailySalesModifierReportDto.getPrice());
                }
                optionDto.setDiscounts(dailySalesModifierReportDto.getDiscountAmount());
                optionDtoListNew.add(optionDto);
                modifierDtoMap.put(dailySalesModifierReportDto.getCategory().trim(), optionDtoListNew);

            } else {
                boolean anyMatch = optionDtoList.stream().anyMatch(optionDto -> optionDto.getOptionName().equalsIgnoreCase(dailySalesModifierReportDto.getOption()));

                if (anyMatch) {
                    optionDtoList.forEach(optionDto -> {
                        if (optionDto.getOptionName().equalsIgnoreCase(dailySalesModifierReportDto.getOption())) {
                            if (dailySalesModifierReportDto.getReceiptType().equalsIgnoreCase("SALE")) {
                                optionDto.setItemsSold(optionDto.getItemsSold() + 1);
                                optionDto.setGrossSale(Formatter.round(optionDto.getGrossSale() + dailySalesModifierReportDto.getPrice(), 2));
                            }
                            if (dailySalesModifierReportDto.getReceiptType().equalsIgnoreCase("REFUND")) {
                                optionDto.setItemRefunded(optionDto.getItemRefunded() + 1);
                                optionDto.setRefunds(optionDto.getRefunds() + dailySalesModifierReportDto.getPrice());
                            }
                            optionDto.setDiscounts(Formatter.round(optionDto.getDiscounts() + dailySalesModifierReportDto.getDiscountAmount(), 2));
                        }
                    });
                } else {
                    OptionDto optionDto = new OptionDto();
                    optionDto.setOptionName(dailySalesModifierReportDto.getOption());
                    if (dailySalesModifierReportDto.getReceiptType().equalsIgnoreCase("SALE")) {
                        optionDto.setItemsSold(1);
                        optionDto.setGrossSale(dailySalesModifierReportDto.getPrice());
                    }
                    if (dailySalesModifierReportDto.getReceiptType().equalsIgnoreCase("REFUND")) {
                        optionDto.setItemRefunded(1);
                        optionDto.setRefunds(dailySalesModifierReportDto.getPrice());
                    }
                    optionDto.setDiscounts(dailySalesModifierReportDto.getDiscountAmount());
                    optionDtoList.add(optionDto);

                }

            }
        });

        modifierDtoMap.values().forEach(modifierDtos -> {
            modifierDtos.forEach(modifierDto -> {
                modifierDto.setSumItemsSold(modifierDtos.stream().mapToInt(f -> f.getItemsSold()).sum());
                modifierDto.setSumGrossSale(modifierDtos.stream().mapToDouble(f -> f.getGrossSale()).sum());
                modifierDto.setSumItemRefunded(modifierDtos.stream().mapToInt(f -> f.getItemRefunded()).sum());
                modifierDto.setSumRefunds(modifierDtos.stream().mapToDouble(f -> f.getRefunds()).sum());
                modifierDto.setSumDiscounts(modifierDtos.stream().mapToDouble(f -> f.getDiscounts()).sum());
                modifierDto.setSumNetSales((modifierDto.getSumGrossSale() - ((modifierDto.getSumRefunds() * modifierDto.getItemRefunded() + modifierDto.getDiscounts()))));
            });
        });

        return modifierDtoMap;
    }
}


