package com.quartz_gradle.batch.job_config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableBatchProcessing
public class ExampleFlowJobConfig {

    public final String JOB_NAME = "exampleFlowJob";
    public final JobBuilderFactory jobBuilderFactory;
    public final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public ExampleFlowJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean(JOB_NAME)
    public Job exampleFlowJob(){

        Job exampleJob = jobBuilderFactory.get("exampleFlowJob")
                .start(step(null,null))
                    .on("FAILED")
                    .to(step2(null,null))
                    .on("*")
                    .end()
                .from(step(null,null))
                    .on("*")
                    .to(step3(null,null))
                    .on("*")
                    .end()
                .end()
                .build();

        return exampleJob;
    }

    @Bean(JOB_NAME+"_step")
    @JobScope
    public Step step(@Value("#{jobParameters[value]}") String value,@Value("#{jobParameters[time]}") String time) {
        return stepBuilderFactory.get("step")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Step!");
                    log.info("jobParameter value = {}",value);
                    log.info("jobParameter time = {}",time);
                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(JOB_NAME+"_step2")
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

    @Bean(JOB_NAME+"_step3")
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