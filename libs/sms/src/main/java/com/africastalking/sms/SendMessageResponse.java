package com.africastalking.sms;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public final class SendMessageResponse {

    @SerializedName("SMSMessageData")
    public SmsMessageData data;

    public static final class SmsMessageData {
        @SerializedName("Recipients")
        public List<Recipient> recipients;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
