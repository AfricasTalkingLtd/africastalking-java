package com.africastalking.payments.recipient;

import com.africastalking.payments.BankAccount;

import java.util.Map;

public final class Bank {

    public String currencyCode;
    public float amount;
    public BankAccount bankAccount;
    public String narration;
    public Map<String, String> metadata;

    public Bank(BankAccount bankAccount, String amount, Map<String, String> metadata) {
        this.bankAccount = bankAccount;
        this.metadata = metadata;

        try {
            String[] currenciedAmount = amount.trim().split(" ");
            this.currencyCode = currenciedAmount[0];
            this.amount = Float.parseFloat(currenciedAmount[1]);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
