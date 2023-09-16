package com.example.demo.service;

import com.example.demo.proxy.ApiProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Slf4j
@Service
public class InitiazeApp {

    @Autowired
    private ApiProxy apiProxy;


    //Entry point to run any example
    public void runApp() throws ExecutionException, InterruptedException {
        getAndMergeApi(5);
    }


    // Example 1
    private void getAndMergeTwoApi() throws ExecutionException, InterruptedException {
        log.info("Execution started calling two api API 1 and API 2");
        long startTime = System.currentTimeMillis();
        CompletableFuture<String> api1ResponseCompletable = CompletableFuture.supplyAsync(() -> apiProxy.api1("api1"));
        CompletableFuture<String> api2ResponseCompletable = CompletableFuture.supplyAsync(() -> apiProxy.api2("api2"));

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(api1ResponseCompletable, api2ResponseCompletable);

        // Wait for both API calls to complete
        combinedFuture.join();

        // Get the results from the CompletableFutures
        String api1Result = api1ResponseCompletable.get();
        String api2Result = api2ResponseCompletable.get();
        long endTime = System.currentTimeMillis();
        log.info("Execution Complete for two api API 1 and API 2 Time -{}", endTime - startTime);

    }

    //Example 2
    private void getAndMergeThreeApi() throws ExecutionException, InterruptedException {
        log.info("Execution started calling 3 api API 1 and API 2 and API 3");

        long startTime = System.currentTimeMillis();
        CompletableFuture api1ResponseCompletable = CompletableFuture.runAsync(() -> {
            try {
                getAndMergeTwoApi();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        CompletableFuture<String> api3ResponseCompletable = CompletableFuture.supplyAsync(() -> apiProxy.api3("api2"));
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(api1ResponseCompletable, api3ResponseCompletable);

        // Wait for both API calls to complete
        combinedFuture.join();

        String api3Result = api3ResponseCompletable.get();
        long endTime = System.currentTimeMillis();
        log.info("Execution Complete for API 1 API 2 API 3 Time -{}", endTime - startTime);


    }


    //Example 3
    private void getAndMergeTwoApiWithException() throws ExecutionException, InterruptedException {
        log.info("Execution started calling two api API 1 and API 4");
        long startTime = System.currentTimeMillis();

        CompletableFuture<String> api1ResponseCompletable = CompletableFuture.supplyAsync(() -> apiProxy.api1("api1"));
        CompletableFuture<String> api4ResponseCompletable = CompletableFuture.supplyAsync(() -> apiProxy.api4("api4")).exceptionally(ex -> {
            log.error("Api 4 threw  exception msg-{} ", ex.getMessage());
            return "Fallback Response";
        });

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(api1ResponseCompletable, api4ResponseCompletable);

        // Wait for both API calls to complete
        combinedFuture.join();

        // Get the results from the CompletableFutures
        String api1Result = api1ResponseCompletable.get();
        String api4Result = api4ResponseCompletable.get();
        long endTime = System.currentTimeMillis();
        log.info("Execution Complete for two api API 1 and API 4 Time -{}", endTime - startTime);
        log.info("api1Result -{} api4Result-{} ", api1Result, api4Result);

    }

    //Example 4
    private void getAndMergeApi(Integer n) throws ExecutionException, InterruptedException {
        log.info("Execution started calling {} api API", n);
        long startTime = System.currentTimeMillis();

        List<CompletableFuture<String>> futureList = new ArrayList<>();
        for (Integer i = 1; i <= n; i++) {
            String param = i.toString();
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> apiProxy.api(param));
            futureList.add(future);
        }

        // Wait for all CompletableFuture to complete and collect the results
        List<Integer> resultList = new ArrayList<>();
        for (Integer i = 1; i <= n; i++) {
            String result = futureList.get(i - 1).get();
            log.info("API RESPONSE FOR PARAM {} is {}", i, result);
        }
        long endTime = System.currentTimeMillis();
        log.info("Execution Complete for {} api  Time -{}", n, endTime - startTime);

    }


    private String handleException(Exception e) {
        log.error("Exception msg-{}", e.getMessage());
        return "Fallback Value";
    }


}
