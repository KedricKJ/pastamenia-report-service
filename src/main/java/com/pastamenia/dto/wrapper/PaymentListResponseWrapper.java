package com.pastamenia.dto.wrapper;

import com.pastamenia.enums.RestApiResponseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class PaymentListResponseWrapper<T> extends BaseResponseWrapper {

    private List<T> content;

    private Pagination pagination;

    private TotalPaymentData paymentData;

    public PaymentListResponseWrapper(List<T> content, Pagination pagination,TotalPaymentData paymentData) {
        super(RestApiResponseStatus.OK);
        this.content = content;
        this.pagination = pagination;
        this.paymentData = paymentData;
    }

    @Data
    public static class Pagination {

        public Pagination(Integer pageNumber, Integer pageSize, Integer totalPages, Long totalRecords) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.totalPages = totalPages;
            this.totalRecords = totalRecords;
        }

        private Integer pageNumber;

        private Integer pageSize;

        private Integer totalPages;

        private Long totalRecords;

    }

    @Data
    public static class TotalPaymentData {

        private String branch;

        BigDecimal cashPayments = new BigDecimal(0.0);
        BigDecimal chequePayments = new BigDecimal(0.0);
        BigDecimal creditPayments = new BigDecimal(0.0);
        BigDecimal creditCardPayments = new BigDecimal(0.0);
        BigDecimal debitCardPayments = new BigDecimal(0.0);
        BigDecimal hangerPayments = new BigDecimal(0.0);
        BigDecimal totalPayments = new BigDecimal(0.0);
        Integer totalQty =0;

    }




}
