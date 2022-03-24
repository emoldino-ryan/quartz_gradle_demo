package com.quartz_gradle.mock_module;

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
                .initialOffsetMs(1000)
                .build();

        service.schedule(info);
    }

    public void runHelloJobOnCluster() {
        final TimerInfo info = TimerInfo.builder()
                .initialOffsetMs(1000)
                .cluster(true)
                .build();

        service.schedule(info);
    }

    public void runJob(TimerInfo info) {
        service.schedule(info);
    }
}
