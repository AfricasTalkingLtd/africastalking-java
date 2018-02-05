package com.africastalking.sms;

import com.google.gson.Gson;

public final class Subscription {
    public long id;
    public String phoneNumber;
    public String date;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
