package com.pastamenia.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Pasindu Lakmal
 */
@Entity
@Getter
@Setter
@Table(name = "line_itm_line_discount")
public class LineItemLineDiscount extends CreateModifyBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receiptTotalDiscountNo;

    @ManyToOne
    @JoinColumn(name = "receipt_line_item_id", nullable = false)
    private ReceiptLineItem receiptLineItem;

    private String id;

    private String pricingType;

    private String type;

    private String name;

    private Integer percentage;

    private Double moneyAmount;

}
