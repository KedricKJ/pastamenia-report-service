package com.pastamenia.dto.response;

import com.pastamenia.dto.reportDto.OptionDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class DailySaleMixReportModifiersReportResponse {

  private String companyName;

  private LocalDate date;

  Map<String, List<OptionDto>> categoryList;


}
