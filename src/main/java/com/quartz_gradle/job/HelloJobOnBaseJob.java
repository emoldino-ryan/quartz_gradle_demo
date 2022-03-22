package com.quartz_gradle.job;

import com.quartz_gradle.job.model.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.impl.jdbcjobstore.JobStoreTX;

/**
 *
 * ########### step2 ###########
 * HelloJob 을 베이스잡
 */
@Slf4j
public class HelloJobOnBaseJob extends BaseJob {

    @Override
    protected void doExecute(JobExecutionContext context) {
        log.info("HERE !!!!!!! I'M PROCESSING ");
        log.info("####################################### Hello Job is being executed!");

        log.info("### {} is being executed!",
                context.getJobDetail().getJobDataMap().get("JobName").toString());
    }
}