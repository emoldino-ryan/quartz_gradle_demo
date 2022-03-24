package com.quartz_gradle.job.service;

import lombok.Data;

@Data
public class SchedulerIn {
    private long initialOffsetMs;
    private boolean cluster;
    private String cronExpress;

    private String serviceName;
    private String method;

    private String groupName;
    private String jobIdentity;
}
