package com.quartz_gradle.job.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class TimerInfo implements Serializable {
    private int totalFireCount;
    private boolean runForever;
    private boolean clustered;
    private long repeatIntervalMs;
    private long initialOffsetMs;
    private String callbackData;
    private boolean cronSchedule;

}
