package com.africastalking.payment.recipient;

import com.africastalking.payment.BankAccount;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public final class Bank {

    public String currencyCode;
    public float amount;
    public BankAccount bankAccount;
    public String narration;
    public Map<String, String> metadata;

    public Bank(BankAccount bankAccount, String amount, String narration, Map<String, String> metadata) {
        this.bankAccount = bankAccount;
        this.metadata = metadata;
        this.narration = narration;

        try {
            String[] currenciedAmount = amount.trim().split(" ");
            this.currencyCode = currenciedAmount[0];
            this.amount = Float.parseFloat(currenciedAmount[1]);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
