package com.emrheathgroup.backend.Enum;

public enum SCHEDULE_TYPE {

    TELE("Tele"),
    EMERGENCY("Emergency"),
    WAITING("Waiting");

    private String value;

    SCHEDULE_TYPE(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
