package com.quartz_gradle.mock_module;

import com.quartz_gradle.job.HelloJob;
import com.quartz_gradle.job.model.TimerInfo;
import com.quartz_gradle.job.service.MMSSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SomeModuleService {
    private final MMSSchedulerService service;

    @Autowired
    public SomeModuleService(final MMSSchedulerService service) {
        this.service = service;
    }

    public void runHelloJob(){
        final TimerInfo info = TimerInfo.builder()
                .totalFireCount(5)
                .repeatIntervalMs(5000)
                .initialOffsetMs(1000)
                .callbackData("괜찮냐")
                .clustered(false)
                .build();

        service.schedule(HelloJob.class,info);
    }

    public void runHelloJobOnCluster() {
        final TimerInfo info = TimerInfo.builder()
                .totalFireCount(5)
                .repeatIntervalMs(5000)
                .initialOffsetMs(1000)
                .callbackData("괜찮냐")
                .clustered(true)
                .build();

        service.schedule(HelloJob.class,info);
    }
}
