package com.pastamenia.dto.response;

import com.pastamenia.dto.reportDto.DailySalesMixReportDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class DailySalesMixReportResponseDto {

  private String companyName;

  private LocalDate date;

  Map<String, List<DailySalesMixReportDto>> dailySalesMixReportMap;

  List<DailySalesMixReportDto> dailySalesMixReportList;

  List<DailySalesMixReportResponse> dailySalesMixReportResponseList;
}
