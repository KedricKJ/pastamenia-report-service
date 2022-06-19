package com.pastamenia.enums;

public enum  Currency {

    LKR("LKR", "LKR");

    private String label;

    private String value;

    Currency(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public static Currency getEnum(String s) {
        for (Currency item : Currency.values()) {
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
