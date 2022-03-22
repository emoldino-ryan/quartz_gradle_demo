package com.quartz_gradle.job.service;

import com.quartz_gradle.job.SchedulerUtils;
import com.quartz_gradle.job.model.TimerInfo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.core.jmx.CronTriggerSupport;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MMSSchedulerService {

    private final SchedulerFactoryBean clusteredSchedulerFactoryBean;
    private final SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    public MMSSchedulerService(@Qualifier("clusteredSchedulerFactoryBean") SchedulerFactoryBean clusteredSchedulerFactoryBean,
                               @Qualifier("schedulerFactoryBean") SchedulerFactoryBean schedulerFactoryBean) {
        this.clusteredSchedulerFactoryBean = clusteredSchedulerFactoryBean;
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

//    private final Scheduler scheduler;
//
//    @Autowired
//    public MMSSchedulerService(Scheduler scheduler) {
//        this.scheduler = scheduler;
//    }

    public void schedule(final Class clazz, final TimerInfo info){

        final JobDetail jobDetail = SchedulerUtils.buildJobDetail(clazz,info);
        final Trigger trigger = SchedulerUtils.buildTrigger(clazz,info);
        //final Trigger trigger = CronTriggerSupport.

        try{
            log.info("clusteredSchedulerFactoryBean eq schedulerFactoryBean is {}",clusteredSchedulerFactoryBean.equals(schedulerFactoryBean));
            Scheduler scheduler = info.isClustered() ? clusteredSchedulerFactoryBean.getScheduler() : schedulerFactoryBean.getScheduler();
            scheduler.scheduleJob(jobDetail,trigger);
        }catch (SchedulerException e){
            log.error(e.getMessage(),e);
        }
    }

    public List<TimerInfo> getAllRunningTimer(){
        try {
            List<TimerInfo> collect = getAllRunningTimer(clusteredSchedulerFactoryBean.getScheduler());
            collect.addAll(getAllRunningTimer(schedulerFactoryBean.getScheduler()));
            return collect;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return null;
        }
    }


    private List<TimerInfo> getAllRunningTimer(Scheduler scheduler) throws SchedulerException {
        return scheduler.getJobKeys(GroupMatcher.anyGroup()).stream()
                .map(jobKey -> {
                    try {
                        final JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                        return (TimerInfo) jobDetail.getJobDataMap().get(jobKey.getName());
                    } catch (SchedulerException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @PostConstruct
    public void init(){
        log.info("Init MMSSchedulerService and Scheduler is started.");
        try {
            clusteredSchedulerFactoryBean.getScheduler().start();
            schedulerFactoryBean.getScheduler().start();
        } catch (SchedulerException e) {
            log.error(e.getMessage(),e);
        }
    }

    @PreDestroy
    public void preDestroy(){
        log.info("Destroy MMSSchedulerService bean and Scheduler is shutdown.");
        try {
            clusteredSchedulerFactoryBean.getScheduler().shutdown();
            schedulerFactoryBean.getScheduler().shutdown();
        } catch (SchedulerException e) {
            log.error(e.getMessage(),e);
        }
    }
}
