package com.pastamenia.controller;


import com.pastamenia.dto.reportDto.DailySalesMixReportDto;
import com.pastamenia.dto.request.DailySalesMixReportRequest;
import com.pastamenia.dto.request.DailySalesSummeryReportRequest;
import com.pastamenia.dto.request.ReportRequest;
import com.pastamenia.dto.response.DailySaleMixReportModifiersReportResponse;
import com.pastamenia.dto.response.DailySalesMixReportResponse;
import com.pastamenia.dto.response.DailySalesMixReportResponseDto;
import com.pastamenia.dto.response.DailySalesSummeryReportResponse;
import com.pastamenia.dto.wrapper.ListResponseWrapper;
import com.pastamenia.entity.Company;
import com.pastamenia.service.CompanyService;
import com.pastamenia.service.ReceiptSupportedReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
public class ReportController {

  private ReceiptSupportedReportService receiptSupportedReportService;

  private CompanyService companyService;


  @PostMapping("${app.endpoint.reportsDailySalesSummery}")
  public ResponseEntity<ListResponseWrapper<DailySalesSummeryReportResponse>> DailySalesSummery(
    @Validated @RequestBody DailySalesSummeryReportRequest request) {
    log.info("Store creation start {}",request);
    List<DailySalesSummeryReportResponse> dailySalesSummeryReportResponses = new ArrayList<>();
    List<Company> companyList  = companyService.findAll();
    for (Company company: companyList) {
      DailySalesSummeryReportResponse templateSalesSummary = receiptSupportedReportService.parseThymeleafTemplateForSalesSummaryReport(company,request.getFromDate(),request.getToDate());
      dailySalesSummeryReportResponses.add(templateSalesSummary);
    }
    return new ResponseEntity<>(new ListResponseWrapper<>(dailySalesSummeryReportResponses), HttpStatus.OK);
  }

  @PostMapping("${app.endpoint.reportsDailySalesMixReport}")
  public ResponseEntity<ListResponseWrapper<DailySalesMixReportResponseDto>> DailySalesMixReport(
    @Validated @RequestBody DailySalesMixReportRequest request) {
    log.info("Store creation start {}",request);
    List<DailySalesMixReportResponseDto> dailySalesSummeryMixReportResponses = new ArrayList<>();
    List<Company> companyList  = companyService.findAll();
    for (Company company: companyList) {
      DailySalesMixReportResponseDto templateSalesMixReport = receiptSupportedReportService.parseThymeleafTemplateForDailySaleMixReport(company,request.getFromDate(),request.getToDate());
      dailySalesSummeryMixReportResponses.add(templateSalesMixReport);
    }
    return new ResponseEntity<>(new ListResponseWrapper<>(dailySalesSummeryMixReportResponses), HttpStatus.OK);
  }


  @PostMapping("${app.endpoint.reportsDailySalesMixModifierReport}")
  public ResponseEntity<ListResponseWrapper<DailySaleMixReportModifiersReportResponse>> DailySalesMixModifierReport(
    @Validated @RequestBody ReportRequest request) {
    log.info("Store creation start {}",request);
    List<DailySaleMixReportModifiersReportResponse> dailySalesSummeryMixReportResponses = new ArrayList<>();
    List<Company> companyList  = companyService.findAll();
    for (Company company: companyList) {
      DailySaleMixReportModifiersReportResponse templateSalesMixReport = receiptSupportedReportService.parseThymeleafTemplateForDailySaleMixReportModifiersReport(company,request.getFromDate(),request.getToDate());
      dailySalesSummeryMixReportResponses.add(templateSalesMixReport);
    }
    return new ResponseEntity<>(new ListResponseWrapper<>(dailySalesSummeryMixReportResponses), HttpStatus.OK);
  }

}
