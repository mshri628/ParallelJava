package com.example.demo.service;

import com.example.demo.proxy.ApiProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Slf4j
@Service
public class InitiazeApp {

    @Autowired
    private ApiProxy apiProxy;


    public void runApp() throws ExecutionException, InterruptedException {
        getAndMergeTwoApiWithException();
    }


    private void getAndMergeThreeApi() throws ExecutionException, InterruptedException{
        log.info("Execution started calling 3 api API 1 and API 2 and API 3");

        long startTime = System.currentTimeMillis();
        CompletableFuture api1ResponseCompletable  = CompletableFuture.runAsync(() -> {
            try {
                getAndMergeTwoApi();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<String> api2ResponseCompletable  = CompletableFuture.supplyAsync(() ->apiProxy.api3("api2"));
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(api1ResponseCompletable, api2ResponseCompletable);

        // Wait for both API calls to complete
        combinedFuture.join();

        String api2Result = api2ResponseCompletable.get();
        long endTime = System.currentTimeMillis();
        log.info("Execution Complete for API 1 API 2 API 3 Time -{}",endTime-startTime);



    }
    private void getAndMergeTwoApi() throws ExecutionException, InterruptedException {
        log.info("Execution started calling two api API 1 and API 2");
        long startTime = System.currentTimeMillis();
        CompletableFuture<String> api1ResponseCompletable  = CompletableFuture.supplyAsync(() ->apiProxy.api1("api1"));
        CompletableFuture<String> api2ResponseCompletable  = CompletableFuture.supplyAsync(() ->apiProxy.api2("api2"));

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(api1ResponseCompletable, api2ResponseCompletable);

        // Wait for both API calls to complete
        combinedFuture.join();

        // Get the results from the CompletableFutures
        String api1Result = api1ResponseCompletable.get();
        String api2Result = api2ResponseCompletable.get();
        long endTime = System.currentTimeMillis();
        log.info("Execution Complete for two api API 1 and API 2 Time -{}",endTime-startTime);

    }

    private void getAndMergeTwoApiWithException() throws ExecutionException, InterruptedException {
        log.info("Execution started calling two api API 1 and API 4");
        long startTime = System.currentTimeMillis();
        CompletableFuture<String> api1ResponseCompletable  = CompletableFuture.supplyAsync(() ->apiProxy.api1("api1"));
        CompletableFuture<String> api4ResponseCompletable  = CompletableFuture.supplyAsync(() ->apiProxy.api4("api4")).exceptionally(ex ->{
            log.error("Api 4 threw  exception msg-{} ",ex.getMessage());
            return "Fallback Response";
        });

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(api1ResponseCompletable, api4ResponseCompletable);

        // Wait for both API calls to complete
        combinedFuture.join();

        // Get the results from the CompletableFutures
        String api1Result = api1ResponseCompletable.get();
        String api4Result = api4ResponseCompletable.get();
        long endTime = System.currentTimeMillis();
        log.info("Execution Complete for two api API 1 and API 4 Time -{}",endTime-startTime);
        log.info("api1Result -{} api4Result-{} ",api1Result,api4Result);

    }

    private String handleException(Exception e){
        log.error("Exception msg-{}",e.getMessage());
        return "Fallback Value";
    }








}
