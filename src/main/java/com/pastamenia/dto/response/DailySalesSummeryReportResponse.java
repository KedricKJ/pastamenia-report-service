package com.pastamenia.dto.response;
import com.pastamenia.dto.reportDto.AmountDto;
import com.pastamenia.dto.reportDto.SalesSummaryReportDto;
import com.pastamenia.report.SaleSummaryReport;
import com.pastamenia.report.SaleSummaryReportDineInVoidsAndRefunds;
import lombok.Data;

import java.util.List;

@Data
public class DailySalesSummeryReportResponse {

  private String from;

  private String to;

  private String company;

  //DiningType
  //List<SaleSummaryReport> dataForSaleSummaryReportDiningType;
  //List<SaleSummaryReportDineInVoidsAndRefunds> dataForSaleSummaryReportDineInVoidsAndRefunds;
  List<SalesSummaryReportDto> dataForSaleSummaryReportDiningTypeDto;
  //List<SalesSummaryReportDto> dataForSaleSummaryReportDiningTypeDtoVoidsAndRefunds;

  //Category
  //List<SaleSummaryReport> dataForSaleSummaryReportCategory;

  //List<SaleSummaryReportDineInVoidsAndRefunds> saleSummaryReportCategoryVoidsAndRefunds;
  List<SalesSummaryReportDto> dataForSaleSummaryReportCategoryDto;
  //List<SalesSummaryReportDto> salesSummaryReportCategoryDtoVoidsAndRefunds;

  //PaymentMode
  //List<SaleSummaryReport> dataForSaleSummaryReportPaymentMode;
  //List<SaleSummaryReportDineInVoidsAndRefunds> saleSummaryReportPaymentTypeVoidsAndRefunds;
  List<SalesSummaryReportDto> dataForSaleSummaryReportPaymentModeDto;
  //List<SalesSummaryReportDto> salesSummaryReportPaymentTypeDtoVoidsAndRefunds;

  int totalQuantityDiningType;
  double totalGrossSaleDiningType;
  double totalVoidRefundDiningType;
  double totalDiscountDiningType;
  double grossTotalDiningType;
  double totalTaxDiningType;
  double totalServiceChargeDiningType;
  double totalNetTotalDiningType;
  double totalSaleDiningType;

  int totalQuantityCategory;
  double totalGrossSaleCategory;
  double totalDiscountCategory;
  double grossTotalCategory;
  double totalTaxCategory;
  double totalServiceChargeCategory;
  double totalNetTotalCategory;
  double totalSaleCategory;

  int totalQuantityPaymentMode;
  double totalGrossSalePaymentMode;
  double totalDiscountPaymentMode;
  double grossTotalPaymentMode;
  double totalTaxPaymentMode;
  double totalServiceChargePaymentMode;
  double totalNetTotalPaymentMode;
  double totalSalePaymentMode;

  List<AmountDto> voids;
  List<AmountDto> refunds;
  List<AmountDto> discounts;

  double totalDiscounts;
  double totalVoids;
  double totalRefunds;


}
