package com.quartz_gradle.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

/**

 */
@Slf4j
public abstract class BaseJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        try{
            beforeExecute(context);
            doExecute(context);
            afterExecute(context);
        }catch (Exception e){
            throwExecute(context);
            log.error(e.getMessage(),e);
        }
    }

    protected abstract void doExecute(JobExecutionContext context) throws SchedulerException;

    private void throwExecute(JobExecutionContext context) {
        log.info("%%% Throw executing job %%%");
    }

    private void beforeExecute(JobExecutionContext context) {
        log.info("%%% Before executing job %%%");
    }

    private void afterExecute(JobExecutionContext context) {
        log.info("%%% After executing job %%%");
    }
}