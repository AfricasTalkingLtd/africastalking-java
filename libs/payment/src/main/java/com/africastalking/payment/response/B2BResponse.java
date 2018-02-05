package com.africastalking.payment.response;

import com.google.gson.Gson;

public final class B2BResponse {
    public String status;
    public String transactionId;
    public String transactionFee;
    public String providerChannel;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
