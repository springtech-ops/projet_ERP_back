package org.springtech.springmarket.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import static com.twilio.rest.api.v2010.account.Message.creator;

public class SmsUtils {
    public static String FROM_NUMBER = "+18026591765";
    public static String SID_KEY = "AC448960111b0060851425420180e3e18d";
    public static String TOKEN_KEY = "5b359a640f00ce94b33fb53bb3231229";

    public static void sendSMS(String to, String messageBody) {
        Twilio.init(SID_KEY, TOKEN_KEY);
        Message message = creator(new PhoneNumber("+" + to), new PhoneNumber(FROM_NUMBER), messageBody).create();
        System.out.println(message);
    }
}
