package com.quartz_gradle.mock_module;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HelloService {
    public HelloService() {
        log.info("hello service init");
    }

    public void hello(){
      log.info("H E L L O");
    }
}
