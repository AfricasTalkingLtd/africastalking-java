package com.africastalking.payment;

import com.google.gson.Gson;

public final class WalletTransaction {
    public String value;
    public String balance;
    public String category;
    public String description;
    public String transactionId;
    public Transaction transactionData;
    
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}