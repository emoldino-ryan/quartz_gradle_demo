package com.quartz_gradle.job.service;

import com.quartz_gradle.job.SchedulerUtils;
import com.quartz_gradle.job.impl.ClusterServiceJob;
import com.quartz_gradle.job.model.TimerInfo;
import com.quartz_gradle.job.repo.SchedulerRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
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
    private final SchedulerRepository schedulerRepository;

    @Autowired
    public MMSSchedulerService(@Qualifier("clusteredSchedulerFactoryBean") SchedulerFactoryBean clusteredSchedulerFactoryBean,
                               @Qualifier("schedulerFactoryBean") SchedulerFactoryBean schedulerFactoryBean,
                               SchedulerRepository schedulerRepository) {
        this.clusteredSchedulerFactoryBean = clusteredSchedulerFactoryBean;
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.schedulerRepository = schedulerRepository;
    }

    public void schedule(final TimerInfo info){

        final Class<? extends Job> clazz = ClusterServiceJob.class;
        final JobDetail jobDetail = SchedulerUtils.buildJobDetail(clazz,info);
        final Trigger trigger = SchedulerUtils.buildTrigger(clazz,info);

        try{
            Scheduler scheduler = info.isCluster() ? clusteredSchedulerFactoryBean.getScheduler() : schedulerFactoryBean.getScheduler();
            scheduler.scheduleJob(jobDetail,trigger);
        }catch (SchedulerException e){
            log.error(e.getMessage(),e);
        }
    }


    public List<TimerInfo> getAllRunningTimer(){
        try {
            List<TimerInfo> collect = getAllRunningTimer(clusteredSchedulerFactoryBean.getScheduler());
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
                        log.info(jobKey.getGroup());
                        log.info(jobKey.getName());
                        final JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                        return (TimerInfo) jobDetail.getJobDataMap().get(jobKey.getGroup()+"."+jobKey.getName());
                    } catch (SchedulerException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public TimerInfo getRunningTimer(String id){
        try{
            String[] ids = id.split("\\.");
            String group = ids[0];
            String name = ids[1];
            final JobDetail jobDetail = clusteredSchedulerFactoryBean.getScheduler().getJobDetail(new JobKey(name,group));
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
            String[] ids = id.split("\\.");
            String group = ids[0];
            String name = ids[1];
            final JobDetail jobDetail = clusteredSchedulerFactoryBean.getScheduler().getJobDetail(new JobKey(name,group));
            if(jobDetail == null){
                log.error("Cannot Find jobDetail with id {}",id);
                return;
            }
            jobDetail.getJobDataMap().put(id,info);

            clusteredSchedulerFactoryBean.getScheduler().addJob(jobDetail,true,true);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public Boolean deleteTimer(final String id){
        try {
            String[] ids = id.split("\\.");
            String group = ids[0];
            String name = ids[1];

            clusteredSchedulerFactoryBean.getScheduler().unscheduleJob(new TriggerKey(name,group));
            clusteredSchedulerFactoryBean.getScheduler().deleteJob(new JobKey(name,group));
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
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
