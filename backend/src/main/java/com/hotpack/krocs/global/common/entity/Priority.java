package com.hotpack.krocs.global.common.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Priority {
    LOW("LOW"), 
    MEDIUM("MEDIUM"), 
    HIGH("HIGH"), 
    CRITICAL("CRITICAL");

    private final String value;

    Priority(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
