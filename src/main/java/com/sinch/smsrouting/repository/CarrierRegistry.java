package com.sinch.smsrouting.repository;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class CarrierRegistry {
    private final AtomicBoolean toggle = new AtomicBoolean(true);

    public String fetchCarrier(String number) {
        if (number.startsWith("+61")) {
            if (toggle.getAndSet(!toggle.get())) {
                return "Telstra";
            } else {
                return "Optus";
            }
        } else if (number.startsWith("+64")) {
            return "Spark";
        } else {
            return "Global";
        }
    }

}
