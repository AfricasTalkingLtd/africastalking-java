package com.africastalking;

import com.google.gson.Gson;

public class ATCash {
    public double amount;
    public String currencyCode;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
