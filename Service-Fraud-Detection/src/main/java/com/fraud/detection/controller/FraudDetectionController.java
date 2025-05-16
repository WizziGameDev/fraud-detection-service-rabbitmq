package com.fraud.detection.controller;

import com.fraud.detection.dto.FraudDetectionListResponse;
import com.fraud.detection.dto.FraudDetectionResponse;
import com.fraud.detection.service.FraudDetectionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class FraudDetectionController {

    @Autowired
    private FraudDetectionServiceImpl fraudDetectionService;

    private final ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();

    @QueryMapping("frauds")
    public List<FraudDetectionListResponse> getFrauds() {
        return CompletableFuture.supplyAsync(fraudDetectionService::listFraud, virtualExecutor).join();
    }

    @QueryMapping("fraudById")
    public FraudDetectionResponse getFraudById(@Argument("id") Integer id) {
        return CompletableFuture.supplyAsync(() -> fraudDetectionService.fraudById(id), virtualExecutor).join();
    }

}
