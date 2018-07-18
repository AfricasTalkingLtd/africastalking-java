package com.africastalking.payment.response;

import com.google.gson.Gson;
import com.africastalking.payment.Transaction;

import java.util.List;

public final class FetchTransactionsResponse {
    public String status;
    public String errorMessage;
    public List<Transaction> responses;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
