package com.pastamenia.enums;

public enum SyncStatus {

    PENDING("Pending", "PENDING"),
    STARTED("Started", "STARTED"),
    COMPLETED("Completed", "COMPLETED"),
    FAILED("Failed", "FAILED");

    private String label;

    private String value;

    SyncStatus(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public static SyncStatus getEnum(String s) {
        for (SyncStatus item : SyncStatus.values()) {
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
