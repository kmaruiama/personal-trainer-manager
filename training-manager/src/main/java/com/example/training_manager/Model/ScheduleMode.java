package com.example.training_manager.Model;

import lombok.Getter;

@Getter
public enum ScheduleMode {
    BY_DAY("DAY"),
    BY_ORDER("ORDER");

    private final String scheduleMode;

    ScheduleMode(String scheduleMode) {
        this.scheduleMode = scheduleMode;
    }
}
