package com.quartz_gradle.job;

import com.quartz_gradle.job.model.TimerInfo;
import org.quartz.*;
import java.util.Date;

public final class SchedulerUtils {
    private SchedulerUtils() { }

    public static JobDetail buildJobDetail(final Class<? extends Job> jobClass, final TimerInfo info){
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(jobClass.getSimpleName(),info);
        jobDataMap.put("service",info.getServiceName());
        jobDataMap.put("method",info.getMethod());

        return JobBuilder
                .newJob(jobClass)
                //.withIdentity(jobClass.getSimpleName())
                .withIdentity(info.getJobIdentity(),info.getGroupName())
                .setJobData(jobDataMap)
                .build();
    }

    public static Trigger buildTrigger(final Class<? extends Job> jobClass, final TimerInfo info){

        return TriggerBuilder.newTrigger()
                //.withIdentity(jobClass.getSimpleName())
                .withIdentity(info.getJobIdentity(),info.getGroupName())
                .withSchedule(CronScheduleBuilder.cronSchedule(info.getCronExpress()))
                .startAt(new Date(System.currentTimeMillis() + info.getInitialOffsetMs()))
                .build();
    }

}
