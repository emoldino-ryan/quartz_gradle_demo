package com.quartz_gradle.job;

import com.emoldino.framework.util.BeanUtils;
import com.quartz_gradle.job.impl.ClusterServiceJob;
import com.quartz_gradle.job.service.MMSSchedulerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class JobTest {

    @Autowired
    MMSSchedulerService service;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

//    @Test
//    public void test() throws SchedulerException, InterruptedException {
//        // jobBuilder 로 JobDetail 처리
//        JobDetail job = newJob(HelloJob.class).build();
//
//        //실행 시점 결정?
//        Trigger trigger = newTrigger().build();
//
//        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
//        defaultScheduler.start();
//        defaultScheduler.scheduleJob(job,trigger);
//
//        Thread.sleep(3 * 1000);
//
//        defaultScheduler.shutdown();
//    }
//

//    @Test
//    public void helloJob() throws SchedulerException, InterruptedException {
//
//        // JobDataMap을 이용해서 원하는 정보 담기
//        JobDataMap jobDataMap = new JobDataMap();
//        jobDataMap.put("JobName", "Job Chain 1");
//
//        // Job 구현 내용이 담긴 HelloJob으로 JobDetail 생성
//        JobDetail jobDetail = newJob(HelloJobOnBaseJob.class)
//                .usingJobData(jobDataMap)  // <- jobDataMap 주입
//                .build();
//
//
//        Trigger trigger = newTrigger().build();
//
//        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
//        defaultScheduler.start();
//        defaultScheduler.scheduleJob(jobDetail,trigger);
//
//        Thread.sleep(3 * 1000);
//
//
//        defaultScheduler.shutdown();
//    }

    @Test
    public void jobService() throws SchedulerException, InterruptedException {
        //        // JobDataMap을 이용해서 원하는 정보 담기
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("service", "com.quartz_gradle.mock_module.HelloSerivce");
        jobDataMap.put("method", "hello");

        // Job 구현 내용이 담긴 HelloJob으로 JobDetail 생성
        JobDetail jobDetail = newJob(ClusterServiceJob.class)
                .usingJobData(jobDataMap)  // <- jobDataMap 주입
                .build();


        Trigger trigger = newTrigger().build();
        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
        defaultScheduler.start();
        defaultScheduler.scheduleJob(jobDetail,trigger);

        Thread.sleep(3 * 1000);

        defaultScheduler.shutdown();

    }

    @Test
    public void 리플렉션테스트(){
//        String serviceName = "com.quartz_gradle.mock_module.HelloSerivceTest";
//        String method = "hello";
//
//        try {
//            Class<?> targetClass = Class.forName(serviceName);
//            Object target = BeanUtils.get(targetClass);
//
//            Method[] declaredMethods = ReflectionUtils.getDeclaredMethods(targetClass);
//            for(Method m : declaredMethods){
//                if(m.getName().equals(method)){
//                    ReflectionUtils.invokeMethod(m,target);
//                    return;
//                }
//            }
//
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }

}

