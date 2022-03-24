package com.quartz_gradle.job.impl;

import com.emoldino.framework.util.BeanUtils;
import com.quartz_gradle.job.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@Slf4j
@DisallowConcurrentExecution
public class NonClusterServiceJob extends ClusterServiceJob {
    @Override
    protected void doExecute(JobExecutionContext context) throws SchedulerException {
        log.info("Wrap the JobClass for NonCluster");
        super.doExecute(context);
    }
}
