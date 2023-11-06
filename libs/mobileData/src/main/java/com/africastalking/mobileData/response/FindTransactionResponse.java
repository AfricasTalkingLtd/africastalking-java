package com.africastalking.mobileData.response;

import com.google.gson.Gson;
import com.africastalking.mobileData.Transaction;

import java.util.List;

public final class FindTransactionResponse {
    public Transaction data;
    public String status;
    public String errorMessage;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
