package com.sinch.smsrouting.exception;

public class OptedOutException extends Exception {

    public OptedOutException(String optedOut) {
        super(optedOut);
    }
}
