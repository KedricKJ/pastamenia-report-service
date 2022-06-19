package com.pastamenia.dto.reportDto;

import lombok.Data;

import java.util.List;

/**
 * @author Pasindu Lakmal
 */
@Data
public class SettlementModeWiseReportDtoData {

    List<ReportData> reportDataList;
    double subTotal;

    @Data
   public static class ReportData {

        String settlement;

        String orderNo;

        String orderType;

        String createdAt;

        String userName;

        String remark;

        Double sale;
    }


}
