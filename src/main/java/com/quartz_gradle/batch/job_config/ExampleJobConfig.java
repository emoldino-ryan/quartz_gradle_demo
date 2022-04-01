package com.quartz_gradle.batch.job_config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableBatchProcessing
public class ExampleJobConfig {

    public final JobBuilderFactory jobBuilderFactory;
    public final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public ExampleJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job exampleJob(){

        Job exampleJob = jobBuilderFactory.get("exampleJob")
                .start(step(null,null))
                .next(step2(null,null))
                .next(step3(null,null))
                .build();

        return exampleJob;
    }

    @Bean
    @JobScope
    public Step step(@Value("#{jobParameters[value]}") String value,@Value("#{jobParameters[time]}") String time) {
        return stepBuilderFactory.get("step")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Step!");
                    log.info("jobParameter value = {}",value);
                    log.info("jobParameter time = {}",time);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    @JobScope
    public Step step2(@Value("#{jobParameters[value]}") String value,@Value("#{jobParameters[time]}") String time) {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Step2!");
                    log.info("jobParameter value = {}",value);
                    log.info("jobParameter time = {}",time);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    @JobScope
    public Step step3(@Value("#{jobParameters[value]}") String value,@Value("#{jobParameters[time]}") String time) {
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Step3!");
                    log.info("jobParameter value = {}",value);
                    log.info("jobParameter time = {}",time);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


}