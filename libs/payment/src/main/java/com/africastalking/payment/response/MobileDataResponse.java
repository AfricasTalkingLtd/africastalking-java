package com.africastalking.payment.response;

import com.google.gson.Gson;

import java.util.List;

public final class MobileDataResponse {

    public String errorMessage = null;
    public List<MobileDataEntry> entries;

    public static class MobileDataEntry {
        public String phoneNumber;
        public String status;
        public String provider;
        public String value;
        public String transactionId;
        public String errorMessage = null;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
