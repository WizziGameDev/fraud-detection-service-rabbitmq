package com.fraud.detection.repository;

import com.fraud.detection.entity.FraudDetection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FraudDetectionRepository extends JpaRepository<FraudDetection, Integer> {

    List<FraudDetection> findAllByUserId(Integer userId);
}
