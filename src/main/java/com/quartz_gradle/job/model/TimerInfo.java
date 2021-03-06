package com.quartz_gradle.job.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class TimerInfo implements Serializable {

    private long initialOffsetMs;
    private boolean cluster;
    private String cronExpress;

    private String serviceName;
    private String method;

    private String groupName;
    private String jobIdentity;

}
