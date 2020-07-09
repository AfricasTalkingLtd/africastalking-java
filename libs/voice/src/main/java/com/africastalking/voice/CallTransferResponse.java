package com.africastalking.voice;


import com.google.gson.Gson;

public final class CallTransferResponse {
    public String status;
    public String errorMessage;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
