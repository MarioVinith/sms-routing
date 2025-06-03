package com.sinch.smsrouting.util;

import java.util.function.Predicate;

public class SmsRoutingUtil {

    private static final String NZ_PHONE_REGEX = "\\+64(2\\d{1})\\d{6,7}";
    private static final String AU_PHONE_REGEX = "\\+614\\d{8}";

    public static final Predicate<String> isValidAUNumber = (number) -> number.matches(AU_PHONE_REGEX);

    public static final Predicate<String> isValidNZNumber = (number) -> number.matches(NZ_PHONE_REGEX);

    public static boolean isValidNumber(String number) {
        if (number.startsWith("+61")) {
            return isValidAUNumber.test(number);
        } else if (number.startsWith("+64")) {
            return isValidNZNumber.test(number);
        } else {
            return true;
        }
    }
}
