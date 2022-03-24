package com.quartz_gradle.job.service;

import com.quartz_gradle.job.SchedulerUtils;
import com.quartz_gradle.job.impl.ClusterServiceJob;
import com.quartz_gradle.job.impl.NonClusterServiceJob;
import com.quartz_gradle.job.model.TimerInfo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
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

    public void schedule(final TimerInfo info){
        log.info("schedule start");
        Class<? extends Job> clazz = info.isCluster() ? ClusterServiceJob.class : NonClusterServiceJob.class;

        final JobDetail jobDetail = SchedulerUtils.buildJobDetail(clazz,info);
        final Trigger trigger = SchedulerUtils.buildTrigger(clazz,info);

        try{
            Scheduler scheduler = clusteredSchedulerFactoryBean.getScheduler();
            scheduler.scheduleJob(jobDetail,trigger);
        } catch (SchedulerException e){
            log.error(e.getMessage(),e);
        }
    }

//
//    public void schedule2(final Class<? extends Job> clazz, final TimerInfo info){
//
//        final JobDetail jobDetail = SchedulerUtils.buildJobDetail(clazz,info);
//        final Trigger trigger = SchedulerUtils.buildTrigger(clazz,info);
//
//        try{
//            Scheduler scheduler = info.isCluster() ? clusteredSchedulerFactoryBean.getScheduler() : schedulerFactoryBean.getScheduler();
//            scheduler.scheduleJob(jobDetail,trigger);
//        }catch (SchedulerException e){
//            log.error(e.getMessage(),e);
//        }
//    }


    public List<TimerInfo> getAllRunningTimer(){
        try {
            List<TimerInfo> collect = getAllRunningTimer(clusteredSchedulerFactoryBean.getScheduler());
            // collect.addAll(getAllRunningTimer(schedulerFactoryBean.getScheduler()));
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

    public TimerInfo getRunningTimer(String id){
        try{
            final JobDetail jobDetail = clusteredSchedulerFactoryBean.getScheduler().getJobDetail(new JobKey(id));
            if(jobDetail == null){
                log.error("Cannot Find jobDetail with id {}",id);
                return null;
            }
            return (TimerInfo) jobDetail.getJobDataMap().get(id);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateTimer(String id, TimerInfo info) {
        try{
            final JobDetail jobDetail = clusteredSchedulerFactoryBean.getScheduler().getJobDetail(new JobKey(id));
            if(jobDetail == null){
                log.error("Cannot Find jobDetail with id {}",id);
                return;
            }
            jobDetail.getJobDataMap().put(id,info);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
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
