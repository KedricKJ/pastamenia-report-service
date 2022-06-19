package com.pastamenia.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ConsolidatedDailySummaryResponse {

  private String companyName;

  private LocalDate date;


  private double fullTotal;
  private int fullTotalCount;

  private List<ConsolidatedDailySummaryData> consolidatedSummaryList;

  @Data
  public static class ConsolidatedDailySummaryData {

    private String store;

    private double dineIn;

    private double takeAway;

    private double uberEats;

    private double pickMe;

    private double eatMealFirst;

    private double total;
    private int totalCount;



    private int dineInCount;

    private int takeAwayCount;

    private int uberEatsCount;

    private int pickMeCount;

    private int eatMealFirstCount;

    public ConsolidatedDailySummaryData(String store){
      this.store = store;
    }
  }



  public ConsolidatedDailySummaryResponse(){}


}
