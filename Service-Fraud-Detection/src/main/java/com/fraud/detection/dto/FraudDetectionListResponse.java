package com.fraud.detection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FraudDetectionListResponse {

    private Integer id;

    private String transactionalCode;

    private Boolean isFraud;

    private Long checkedAt;

    private Long transactionCreated;

    private String reason;
}
