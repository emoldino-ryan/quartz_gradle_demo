package com.quartz_gradle.job.impl;

import com.emoldino.framework.util.BeanUtils;
import com.quartz_gradle.job.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

@Slf4j
public class ClusterServiceJob extends BaseJob {

//    @Autowired
//    private JobLauncher jobLauncher;

    @Override
    protected void doExecute(JobExecutionContext context) throws SchedulerException {

        try{

            JobParameters jobParameters = new JobParametersBuilder()
                    //.addString("input.file.name", fileName)
                    .addString("value","value")
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            Job job = BeanUtils.get("exampleFlowJob");
            //.get("exampleJob")
            BeanUtils.get(JobLauncher.class).run(job,jobParameters);
            //jobLauncher.run(job, jobParameters);

//        String serviceName = context.getJobDetail().getJobDataMap().getString("service");
//        String method = context.getJobDetail().getJobDataMap().getString("method");
//
//        try {
//            Class<?> targetClass = Class.forName(serviceName);
//            Object target = BeanUtils.get(targetClass);
//
//            Method[] declaredMethods = ReflectionUtils.getDeclaredMethods(targetClass);
//            for(Method m : declaredMethods){
//                if(m.getName().equals(method)){
//
//                    log.info("serviceName is {}",serviceName);
//                    log.info("method is {}",method);
//
//                    m.invoke(target,new Object[0]);
//                    return;
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
