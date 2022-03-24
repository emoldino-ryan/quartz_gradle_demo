package com.quartz_gradle.job.controller;

import com.quartz_gradle.job.model.TimerInfo;
import com.quartz_gradle.job.service.MMSSchedulerService;
import com.quartz_gradle.job.service.SchedulerIn;
import com.quartz_gradle.mock_module.SomeModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class MMSSchedulerController {

    private final MMSSchedulerService service;

    @Autowired
    public MMSSchedulerController(final MMSSchedulerService service) {
        this.service = service;
        log.info("MMSSchedulerController is loaded");
    }

    @PostMapping("/job/run")
    public void fireTrigger(@RequestBody TimerInfo info){
        //info.getInitialOffsetMs();
        service.schedule(info);
    }

    @PostMapping("/job/{id}")
    public void updateTrigger(@PathVariable String id, @RequestBody TimerInfo info){
        //info.getInitialOffsetMs();
        service.updateTimer(id,info);
    }



    @GetMapping("/job")
    public List<TimerInfo> getTimger(){
        return service.getAllRunningTimer();
    }

    @GetMapping("/job/{id}")
    public TimerInfo getTimger(@PathVariable String id){
        return service.getRunningTimer(id);
    }
}
