package com.fraud.detection.service;

import com.fraud.detection.dto.FraudDetectionListResponse;
import com.fraud.detection.dto.FraudDetectionResponse;
import com.fraud.detection.dto.TransactionCheckFraudResponse;

import java.util.List;

public interface FraudDetectionService {

    // Get all fraud
    List<FraudDetectionListResponse> listFraud();

    // Get fraud by id
    FraudDetectionResponse fraudById(Integer id);

    // Processing fraud
    void detectionFraudRules(TransactionCheckFraudResponse request);
}
