package com.pastamenia.entity;//package com.pastamania.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Pasindu Lakmal
 */
@Entity
@Getter
@Setter
@Table(name = "line_item_line_modifier")
public class LineItemLineModifier extends CreateModifyBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lineItemLineModifierNo;

    @ManyToOne
    @JoinColumn(name = "receipt_line_item_id", nullable = false)
    private ReceiptLineItem receiptLineItem;

    private String id;

    private String modifierOptionId;

    private String name;

    private String _option;

    private String price;

    private Double moneyAmount;
}
