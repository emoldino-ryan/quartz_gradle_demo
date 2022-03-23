package com.quartz_gradle.job;

import com.quartz_gradle.job.model.TimerInfo;
import org.quartz.*;
import java.util.Date;

public final class SchedulerUtils {
    private SchedulerUtils() { }

    public static JobDetail buildJobDetail(final Class<? extends Job> jobClass, final TimerInfo info){
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(jobClass.getSimpleName(),info);

        return JobBuilder
                .newJob(jobClass)
                .withIdentity(jobClass.getSimpleName())
                .setJobData(jobDataMap)
                .build();
    }

    public static Trigger buildTrigger(final Class<? extends Job> jobClass, final TimerInfo info){
        SimpleScheduleBuilder simpleScheduleBuilder =
                SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(info.getRepeatIntervalMs());

        if(info.isCronSchedule()){

        }else{
            if(info.isRunForever()){
                simpleScheduleBuilder = simpleScheduleBuilder.repeatForever();
            }else{
                simpleScheduleBuilder = simpleScheduleBuilder.withRepeatCount(info.getTotalFireCount() - 1);
            }
        }


        return TriggerBuilder.newTrigger()
                .withIdentity(jobClass.getSimpleName())
                .withSchedule(simpleScheduleBuilder)
                .startAt(new Date(System.currentTimeMillis() + info.getInitialOffsetMs()))
                .build();
    }

}
