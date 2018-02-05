package com.africastalking.voice;


import com.google.gson.Gson;

public final class CallResponse {
    public String status;
    public String phoneNumber;
    public String errorMessage;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
