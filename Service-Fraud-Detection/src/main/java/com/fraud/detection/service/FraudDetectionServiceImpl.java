package com.fraud.detection.service;

import com.fraud.detection.detection.FraudDetectionUtil;
import com.fraud.detection.dto.FraudDetectionListResponse;
import com.fraud.detection.dto.FraudDetectionResponse;
import com.fraud.detection.dto.FraudValidateResponse;
import com.fraud.detection.dto.TransactionCheckFraudResponse;
import com.fraud.detection.entity.FraudDetection;
import com.fraud.detection.exception.FraudException;
import com.fraud.detection.publisher.RabbitMQProducer;
import com.fraud.detection.repository.FraudDetectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class FraudDetectionServiceImpl implements FraudDetectionService{

    @Autowired
    private FraudDetectionRepository fraudDetectionRepository;

    @Autowired
    private FraudDetectionUtil fraudDetectionUtil;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Override
    public List<FraudDetectionListResponse> listFraud() {

        return fraudDetectionRepository.findAll().stream()
                .map(data ->
                        new FraudDetectionListResponse(
                                data.getId(),
                                data.getTransactionalCode(),
                                data.getIsFraud(),
                                data.getCheckedAt(),
                                data.getTransactionCreated(),
                                data.getReason()))
                .toList();
    }

    @Override
    public FraudDetectionResponse fraudById(Integer id) {

        FraudDetection fraud = fraudDetectionRepository.findById(id).orElseThrow(() -> new FraudException("Not found"));

        return FraudDetectionResponse.builder()
                .id(fraud.getId())
                .transactionalCode(fraud.getTransactionalCode())
                .isFraud(fraud.getIsFraud())
                .reason(fraud.getReason())
                .checkedAt(fraud.getCheckedAt())
                .totalPrice(fraud.getTotalPrice())
                .channel(fraud.getChannel())
                .userIP(fraud.getUserIP())
                .deviceName(fraud.getDeviceName())
                .location(fraud.getLocation())
                .build();
    }

    @Override
    public void detectionFraudRules(TransactionCheckFraudResponse request) {

        List<FraudDetection> allUserByID = fraudDetectionRepository.findAllByUserId(request.getUserId());

        List<Long> timeAlert = allUserByID.stream().map(data -> data.getCheckedAt()).toList();

        List<Double> pricesAlert = allUserByID.stream().map(data -> data.getTotalPrice()).toList();

        List<String> deviceAlert = allUserByID.stream().map(data -> data.getDeviceName()).toList();

        boolean highFrequency = fraudDetectionUtil.isHighFrequency(request.getCreatedAt(), timeAlert);

        boolean abnormalPrice = fraudDetectionUtil.isAbnormalPrice(request.getTotalPrice(), pricesAlert);

        // Pastikan deviceAlert dan timeAlert tidak kosong sebelum ambil last element
        String lastDevice = deviceAlert.isEmpty() ? "" : deviceAlert.get(deviceAlert.size() - 1);
        long lastTime = timeAlert.isEmpty() ? 0L : timeAlert.get(timeAlert.size() - 1);

        boolean deviceSwitchInRapidTime = fraudDetectionUtil.isDeviceSwitchInRapidTime(lastDevice, request.getDeviceName(), lastTime, request.getCreatedAt());

        StringBuilder reasonBuilder = new StringBuilder();

        boolean isFraud = highFrequency || abnormalPrice || deviceSwitchInRapidTime;

        if (highFrequency) {
            reasonBuilder.append("High Frequency");
        }
        if (abnormalPrice) {
            if (reasonBuilder.length() > 0) reasonBuilder.append(" | ");
            reasonBuilder.append("Abnormal Price");
        }
        if (deviceSwitchInRapidTime) {
            if (reasonBuilder.length() > 0) reasonBuilder.append(" | ");
            reasonBuilder.append("Device Switch in Rapid Time");
        }

        String reason = reasonBuilder.toString();

        FraudDetection fraudDetection = new FraudDetection();
        fraudDetection.setUserId(request.getUserId());
        fraudDetection.setTransactionId(request.getTransactionId());
        fraudDetection.setTransactionalCode(request.getTransactionalCode());
        fraudDetection.setIsFraud(isFraud);
        fraudDetection.setReason(reason);
        fraudDetection.setCheckedAt(Instant.now().getEpochSecond());
        fraudDetection.setTotalPrice(request.getTotalPrice());
        fraudDetection.setChannel(request.getChannel());
        fraudDetection.setUserIP(request.getUserIP());
        fraudDetection.setLocation(request.getLocation());
        fraudDetection.setDeviceName(request.getDeviceName());
        fraudDetection.setTransactionCreated(request.getCreatedAt());

        FraudDetection fraud = fraudDetectionRepository.save(fraudDetection);

        FraudValidateResponse responseToTransaction = FraudValidateResponse.builder()
                .transactionId(fraud.getTransactionId())
                .transactionalCode(fraud.getTransactionalCode())
                .isFraud(fraud.getIsFraud())
                .reason(fraud.getReason())
                .checkedAt(fraud.getCheckedAt())
                .build();

        rabbitMQProducer.sendMessage(responseToTransaction);
    }
}
