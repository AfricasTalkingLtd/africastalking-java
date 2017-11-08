package com.africastalking.sms;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public final class FetchMessageResponse {

    @SerializedName("SMSMessageData")
    public SmsMessageData data;

    public static final class SmsMessageData {
        @SerializedName("Recipients")
        public List<Message> messages = new ArrayList<>();
    }
}
