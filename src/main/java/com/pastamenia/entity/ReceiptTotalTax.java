package com.pastamenia.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Pasindu Lakmal
 */
@Entity
@Getter
@Setter
@Table(name = "receipt_total_tax")
public class ReceiptTotalTax  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receiptTotalTaxNo;

    @ManyToOne
    @JoinColumn(name = "receipt_id", nullable = false)
    private Receipt receipt;

    private String id;

    private String type;

    private String name;

    private BigDecimal rate;

    private BigDecimal moneyAmount;

}
