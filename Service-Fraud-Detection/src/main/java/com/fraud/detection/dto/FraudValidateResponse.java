package com.fraud.detection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FraudValidateResponse {

    private Integer transactionId;

    private String transactionalCode;

    private Boolean isFraud;

    private String reason;

    private Long checkedAt;
}
