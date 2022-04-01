package com.quartz_gradle.batch_job;

import com.emoldino.framework.util.BeanUtils;
import com.quartz_gradle.batch.job_config.ExampleJobConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={ExampleJobConfig.class,TestBatchLegacyConfig.class})
public class BatchJobTest {

//    @Autowired
//    public JobBuilderFactory jobBuilderFactory;
//    @Autowired
//    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;


//    @Autowired(required = true)
//    JobLauncher launcher ;
//    @Autowired
//    @Qualifier("exampleJob")
//    Job job;


    @Test
    public void 테스트(){
        Assert.assertEquals(true,true);
    }


    @Test
    public void jobTest() throws Exception {
        JobParameters dd = new JobParametersBuilder().toJobParameters();
        //launcher.run(job,dd);
        jobLauncherTestUtils.launchJob(dd);

    }
}
