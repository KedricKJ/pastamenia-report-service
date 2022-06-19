package com.pastamenia.enums;

public enum TaxType {

    SERVICE_CHARGE("Service Charge", "SERVICE_CHARGE");

    private String label;

    private String value;

    TaxType(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public static TaxType getEnum(String s) {
        for (TaxType item : TaxType.values()) {
            if (item.value.equals(s)) {
                return item;
            }
        }

        return null;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
