package com.fraud.detection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCheckFraudResponse {
    private Integer userId;
    private Integer transactionId;
    private String transactionalCode;
    private String productName;
    private Integer quantity;
    private Double price;
    private Double totalPrice;
    private String status;
    private Long createdAt;
    private Long updatedAt;
    private Boolean deleted;

    private String channel;
    private String userIP;
    private String deviceName;
    private String location;
}
