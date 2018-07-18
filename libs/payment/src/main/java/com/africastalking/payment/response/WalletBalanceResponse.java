package com.africastalking.payment.response;

import com.google.gson.Gson;

import java.util.List;

public final class WalletBalanceResponse {
    public String status;
    public String errorMessage;
    public String balance;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
