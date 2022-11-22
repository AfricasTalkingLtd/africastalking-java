package com.africastalking.chat;

import com.google.gson.Gson;

public final class ConsentResponse {
    public String customerId;
    public String status;
    public int statusCode;
    public String description;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
