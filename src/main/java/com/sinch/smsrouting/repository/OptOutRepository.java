package com.sinch.smsrouting.repository;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class OptOutRepository {
    private final Set<String> optedOutNumbers = Collections.synchronizedSet(new HashSet<>());

    public void optOut(String number) {
        optedOutNumbers.add(number);
    }

    public boolean isOptedOut(String number) {
        return optedOutNumbers.contains(number);
    }
}
