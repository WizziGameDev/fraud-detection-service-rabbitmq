package com.fraud.detection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FraudDetectionResponse {

    private Integer id;

    private String transactionalCode;

    private Boolean isFraud;

    private String reason;

    private Long checkedAt;

    private Double totalPrice;

    private String channel;

    private String userIP;

    private String deviceName;

    private String location;
}
