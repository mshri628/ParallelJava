package com.example.demo.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiProxy {


    public String api(String arg)  {
        log.info("API  Execution started for param-{} ThreadName-{}",arg,Thread.currentThread().getName());
        sleep(3000);
        log.info("API  Execution complete for param-{}",arg);
        return "RESPONSE-API"+arg;
    }

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

    public String api4(String arg)  {
        log.info("API 4 Execution ThreadName-{}",Thread.currentThread().getName());
        sleep(5000);
        log.info("API 4 Execution complete");
        throw new NullPointerException("Null String ");
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
