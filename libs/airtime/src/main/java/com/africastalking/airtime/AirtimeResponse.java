package com.africastalking.airtime;


import com.google.gson.Gson;

import java.util.List;

public class AirtimeResponse {

    public int numSent;
    public String totalAmount;
    public String totalDiscount;
    public String errorMessage;
    public List<AirtimeEntry> responses;

    public static class AirtimeEntry {
        public String errorMessage;
        public String phoneNumber;
        public String amount;
        public String discount;
        public String status;
        public String requestId;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
