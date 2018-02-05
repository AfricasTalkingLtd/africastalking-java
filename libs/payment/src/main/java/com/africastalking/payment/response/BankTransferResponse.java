package com.africastalking.payment.response;

import com.google.gson.Gson;

import java.util.List;

public final class BankTransferResponse {
    public String errorMessage;
    public List<BankEntries> entries;

    public static final class BankEntries {
        public String accountNumber;
        public String status;
        public String transactionId;
        public String transactionFee;
        public String errorMessage;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
