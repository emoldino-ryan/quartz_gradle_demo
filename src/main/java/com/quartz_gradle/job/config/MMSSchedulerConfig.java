package com.quartz_gradle.job.config;

import com.quartz_gradle.job.config.listener.MMSJobListener;
import com.quartz_gradle.job.config.listener.MMSTriggerListener;
import org.quartz.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
public class MMSSchedulerConfig {

    @Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource quartzDataSource() { return DataSourceBuilder.create().build(); }

    @Bean
    public TriggerListener listener(){
        return new MMSTriggerListener();
    }

    @Bean
    public JobListener jobListener(){
        return new MMSJobListener();
    }

    /**
     * Cluster Schduler Bean
     * @param quartzDataSource
     * @param listener
     * @param jobListener
     * @return
     */
    @Bean("clusteredSchedulerFactoryBean")
    public SchedulerFactoryBean clusteredSchedulerFactoryBean(
                DataSource quartzDataSource,
                TriggerListener listener,
                JobListener jobListener)
    {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz.properties"));
        schedulerFactoryBean.setDataSource(quartzDataSource);
        schedulerFactoryBean.setGlobalTriggerListeners(listener);
        schedulerFactoryBean.setGlobalJobListeners(jobListener);
        return schedulerFactoryBean;
    }

    /**
     * Non cluster Scheduler Bean
     * @param quartzDataSource
     * @param listener
     * @param jobListener
     * @return
     */
    @Bean("schedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean(
            DataSource quartzDataSource,
            TriggerListener listener,
            JobListener jobListener)
    {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz-no-cluster.properties"));
        schedulerFactoryBean.setDataSource(quartzDataSource);
        schedulerFactoryBean.setGlobalTriggerListeners(listener);
        schedulerFactoryBean.setGlobalJobListeners(jobListener);
        return schedulerFactoryBean;
    }

}
