package com.africastalking.payment.response;

import com.google.gson.Gson;
import com.africastalking.payment.WalletTransaction;

import java.util.List;

public final class WalletTransactionsResponse {
    public String status;
    public String errorMessage;
    public List<WalletTransaction> responses;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
