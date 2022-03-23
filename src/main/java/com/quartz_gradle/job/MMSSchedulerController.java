package com.quartz_gradle.job;

import com.quartz_gradle.job.service.MMSSchedulerService;
import com.quartz_gradle.mock_module.SomeModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/helloJob")
    public void fireTrigger(){
        moduleService.runHelloJob();
    }

    @GetMapping("/helloJob/cluster")
    public void fireTriggerClustered(){
        moduleService.runHelloJobOnCluster();
    }
}
