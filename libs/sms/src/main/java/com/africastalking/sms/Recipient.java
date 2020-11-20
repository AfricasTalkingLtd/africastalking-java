package com.africastalking.sms;


import com.google.gson.Gson;

public final class Recipient {
    public int statusCode;
    public String number;
    public String cost;
    public String status;
    public String messageId;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
