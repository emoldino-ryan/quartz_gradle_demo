package com.quartz_gradle.job.controller;

import com.quartz_gradle.job.model.TimerInfo;
import com.quartz_gradle.job.service.MMSSchedulerService;
import com.quartz_gradle.job.service.SchedulerIn;
import com.quartz_gradle.mock_module.SomeModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class MMSSchedulerController {

    private final MMSSchedulerService service;
    private final SomeModuleService moduleService;

    @Autowired
    public MMSSchedulerController(final MMSSchedulerService service, final SomeModuleService moduleService) {
        this.service = service;
        this.moduleService = moduleService;
        log.info("MMSSchedulerController is loaded");
    }

    @PostMapping("/job/run")
    public void fireTrigger(@RequestBody TimerInfo info){
        //info.getInitialOffsetMs();
        moduleService.runJob(info);
    }

    @GetMapping("/helloJob/cluster")
    public void fireTriggerClustered(){
        moduleService.runHelloJobOnCluster();
    }
}
