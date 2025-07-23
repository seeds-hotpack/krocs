package com.hotpack.krocs.global.common.entity;

import lombok.Getter;

@Getter
public enum RepeatType {
    NONE(0),
    DAILY(1),
    WEEKLY(2),
    MONTHLY(3),
    YEARLY(4);

    private final int value;

    RepeatType(int value) {
        this.value = value;
    }
}
