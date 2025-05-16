package com.fraud.detection.detection;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class FraudDetectionUtil {

    // Frekuensi dalam time seberapa cepat jika ada order 5-10 dalam waktu yang sama
    public boolean isHighFrequency(long currentTimeMillis, List<Long> timestamps) {
        long windowMillis = 60_000;
        long windowStart = currentTimeMillis - windowMillis;

        long count = timestamps.stream()
                .filter(t -> t >= windowStart && t <= currentTimeMillis)
                .count();

        return count >= 5;
    }

    // Abnormal price jika price sebelumnya rendah dan tiba" meninggi
    public boolean isAbnormalPrice(double currentPrice, List<Double> historicalPrices) {
        if (historicalPrices == null || historicalPrices.isEmpty()) return false;

        double average = historicalPrices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return currentPrice > (average * 3); // threshold 3x
    }

    // User sama device berbeda dengan Frequensi transaction yang cepat
    public boolean isDeviceSwitchInRapidTime(String previousDevice, String currentDevice, long lastTxnTime, long currentTime) {
        boolean isDifferentDevice = !currentDevice.equals(previousDevice);
        long timeSinceLastTxn = currentTime - lastTxnTime;
        return isDifferentDevice && timeSinceLastTxn < 60000; // 60 detik
    }

}
