package com.fraud.detection.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "frauds")
public class FraudDetection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "transactional_code")
    private String transactionalCode;

    @Column(name = "is_fraud")
    private Boolean isFraud;

    private String reason; // Hasil fraud jika ada

    @Column(name = "checked_at")
    private Long checkedAt;

    @Column(name = "total_price")
    private Double totalPrice;

    private String channel;

    @Column(name = "user_ip")
    private String userIP;

    @Column(name = "device_name")
    private String deviceName;

    private String location;

    @Column(name = "transaction_created")
    private Long transactionCreated;
}
