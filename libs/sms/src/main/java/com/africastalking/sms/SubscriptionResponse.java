package com.africastalking.sms;


import com.google.gson.Gson;

public final class SubscriptionResponse {
    public String success;
    public String description;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
