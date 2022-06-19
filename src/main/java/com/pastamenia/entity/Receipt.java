package com.pastamenia.entity;


import com.pastamenia.enums.SyncStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

/**
 * @author Pasindu Lakmal
 */
@Entity
@Getter
@Setter
public class Receipt  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receiptNo;

    private String receiptNumber;

    private String note;

    private String receiptType;

    private String refundFor;

    private String _order;

    private String createdAt;

    private String receiptDate;

    private String updatedAt;

    private String cancelledAt;

    private String source;

    private BigDecimal totalMoney;

    private BigDecimal totalTax;

    private BigDecimal pointsEarned;

    private BigDecimal pointsDeducted;

    private BigDecimal pointBalance;

    private String customerId;

    private BigDecimal totalDiscount;

    private String employeeId;

    private String storeId;

    private String posDeviceId;

    private String diningOption;

    private Integer tip;

    private Integer surcharge;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "receipt")
    private Set<ReceiptTotalDiscount> totalDiscounts;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "receipt")
    private Set<ReceiptTotalTax> totalTaxes;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "receipt")
    private Set<ReceiptLineItem> receiptLineItems;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "receipt")
    private Set<ReceiptPayment> payments;

    @Column(columnDefinition = "varchar(50) not null default 'PENDING'")
    private SyncStatus syncStatus = SyncStatus.PENDING;

    @Column(columnDefinition = "int not null default 0")
    private Integer errorCount = 0;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

}
