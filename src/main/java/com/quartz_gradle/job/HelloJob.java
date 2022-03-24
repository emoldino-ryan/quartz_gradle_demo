package com.quartz_gradle.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 */
@Slf4j
public class  HelloJob implements Job {

    @Override
    public void execute(JobExecutionContext context){
        log.info("HERE !!!!!!! I'M PROCESSING ");
        log.info("####################################### Hello Job is being executed!");
    }
}