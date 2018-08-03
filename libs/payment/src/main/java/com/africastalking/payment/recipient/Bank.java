package com.africastalking.payment.recipient;

import com.africastalking.payment.BankAccount;
import com.google.gson.Gson;

import java.util.Map;

public final class Bank {

    public String currencyCode;
    public float amount;
    public BankAccount bankAccount;
    public String narration;
    public Map<String, String> metadata;

    public Bank(BankAccount bankAccount, String currencyCode, float amount, String narration, Map<String, String> metadata) {
        this.amount = amount;
        this.metadata = metadata;
        this.narration = narration;
        this.bankAccount = bankAccount;
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
