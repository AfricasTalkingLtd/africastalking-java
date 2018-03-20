package com.africastalking.payment.response;

import com.google.gson.Gson;

public class WalletTransferResponse {
    public String status;
    public String description;
    public String transactionId;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
