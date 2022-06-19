package com.pastamenia.dto.request;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ReportRequest {

  @NotNull(message = "Please select valid start date")
  private String fromDate;
  @NotNull(message = "Please select valid end date")
  private String toDate;
}
