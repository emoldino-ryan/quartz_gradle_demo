package com.quartz_gradle.job.config;

import com.quartz_gradle.job.config.listener.MMSJobListener;
import com.quartz_gradle.job.config.listener.MMSTriggerListener;
import org.quartz.*;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.util.Properties;

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

    @Bean("clusteredSchedulerFactoryBean")
    public SchedulerFactoryBean clusteredSchedulerFactoryBean(
                ApplicationContext context,
                DataSource quartzDataSource,
                TriggerListener listener,
                JobListener jobListener)
    {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz.properties"));
        schedulerFactoryBean.setDataSource(quartzDataSource);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        schedulerFactoryBean.setGlobalTriggerListeners(listener);
        schedulerFactoryBean.setGlobalJobListeners(jobListener);
        return schedulerFactoryBean;
    }

    @Bean("schedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean(
            ApplicationContext context,
            DataSource quartzDataSource,
            TriggerListener listener,
            JobListener jobListener)
    {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz-no-cluster.properties"));
        schedulerFactoryBean.setDataSource(quartzDataSource);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        schedulerFactoryBean.setGlobalTriggerListeners(listener);
        schedulerFactoryBean.setGlobalJobListeners(jobListener);
        return schedulerFactoryBean;
    }

}
