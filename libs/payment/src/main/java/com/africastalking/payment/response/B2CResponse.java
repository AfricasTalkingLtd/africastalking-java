package com.africastalking.payment.response;

import com.google.gson.Gson;

import java.util.List;

public final class B2CResponse {

    public int numQueued;
    public String totalValue;
    public String totalTransactionFee;
    public List<B2CEntry> entries;


    public static class B2CEntry {
        public String phoneNumber;
        public String status;
        public String provider;
        public String providerChannel;
        public String value;
        public String transactionId;
        public String transactionFee;
        public String errorMessage = null;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
