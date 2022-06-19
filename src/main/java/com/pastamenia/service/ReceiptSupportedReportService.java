package com.pastamenia.service;

import com.pastamenia.dto.reportDto.DailySalesMixReportDto;
import com.pastamenia.dto.response.DailySaleMixReportModifiersReportResponse;
import com.pastamenia.dto.response.DailySalesMixReportResponse;
import com.pastamenia.dto.response.DailySalesMixReportResponseDto;
import com.pastamenia.dto.response.DailySalesSummeryReportResponse;
import com.pastamenia.entity.Company;

import java.util.List;

/**
 * @author Pasindu Lakmal
 */
public interface ReceiptSupportedReportService {

    DailySaleMixReportModifiersReportResponse parseThymeleafTemplateForDailySaleMixReportModifiersReport(Company company , String from , String to) ;

    DailySalesMixReportResponseDto parseThymeleafTemplateForDailySaleMixReport(Company company , String from , String to) ;

    DailySalesSummeryReportResponse parseThymeleafTemplateForSalesSummaryReport(Company company , String from , String to);

    List<DailySalesMixReportDto> getStringCategoryDtoMap(String from, String to, Long companyId);

    /*String parseThymeleafTemplateForSettlementModeWiseReport(Company company ,String from , String to);

    String parseThymeleafTemplateForVoidRefundDetailReport();

    String parseThymeleafTemplateForHourlySaleReport(Company company ,String from , String to);

    String parseThymeleafTemplateForDiscountDetailReport(Company company ,String from , String to);

    String parseThymeleafTemplateForConsolidatedDailySalesSummaryReport(Company company ,String from , String to);*/

}
