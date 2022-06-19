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
@Table(name = "receipt_payment")
public class ReceiptPayment extends CreateModifyBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receiptPaymentNo;

    @ManyToOne
    @JoinColumn(name = "receipt_id", nullable = false)
    private Receipt receipt;

    private String paymentTypeId;

    private String name;

    private String type;

    private BigDecimal moneyAmount;

    private String paidAt;

    private String paymentDetails;

}
