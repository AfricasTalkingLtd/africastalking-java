package com.africastalking.voice;


import com.google.gson.Gson;

public final class QueuedCallsResponse {
    public String phoneNumber;
    public String queueName;
    public int numCalls;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
