package com.mich.todolist.utilities;

public enum SortType {
    ALPHABETICAL(0),
    ALPHABETICAL_DESC(1),
    BY_DATE(2),
    BY_DATE_DESC(3),
    BY_PRIORITY(4),
    BY_PRIORITY_DESC(5);

    int value;

    SortType(int value) {
        this.value = value;
    }

    public static SortType fromValue(int value) {
        for (SortType type : values()) {
            if (type.value == value) {
                return type;
            }
        }

        return ALPHABETICAL;
    }

    public int getValue() {
        return value;
    }
}
