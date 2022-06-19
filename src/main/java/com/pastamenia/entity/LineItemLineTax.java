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
@Table(name = "line_item_line_tax")
public class LineItemLineTax extends CreateModifyBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receiptTotalTaxNo;

    @ManyToOne
    @JoinColumn(name = "receipt_line_item_id", nullable = false)
    private ReceiptLineItem receiptLineItem;

    private String id;

    private String type;

    private String name;

    private BigDecimal rate;

    private BigDecimal moneyAmount;

}
