package com.example.demo.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiProxy {

    public String api1(String arg)  {
        log.info("API 1 Execution started ThreadName-{}",Thread.currentThread().getName());
        sleep(3000);
        log.info("API 1 Execution complete");
        return "RESPONSE-API-1";
    }

    public String api2(String arg) {
        log.info("API 2 Execution ThreadName-{}",Thread.currentThread().getName());
        sleep(5000);
        log.info("API 2 Execution complete");
        return "RESPONSE-API-2";
    }

    public String api3(String arg)  {
        log.info("API 3 Execution ThreadName-{}",Thread.currentThread().getName());
        sleep(10000);
        log.info("API 3 Execution complete");
        return "RESPONSE-API-3";
    }

    public void sleep(long timeInMilli)
    {
        try {
            Thread.sleep(timeInMilli);
        } catch (InterruptedException e) {
            log.error("Exception -{}",e.getMessage());
        }
    }
}
