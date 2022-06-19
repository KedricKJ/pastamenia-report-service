package com.pastamenia.enums;

public enum DateTimePattern {

    STRICT_DATE("uuuu-MM-dd"), STRICT_TIME("HH:mm"), STRICT_DATE_TIME("uuuu-MM-dd'T'HH:mm:ss"),STRICT_DATE_TIME_WITHOUT_ZONE("uuuu-MM-dd HH:mm:ss");

    private String pattern;

    DateTimePattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
