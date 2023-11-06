package com.africastalking.mobileData.response;

import com.google.gson.Gson;

public final class WalletBalanceResponse {
    public String status;
    public String errorMessage;
    public String balance;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
