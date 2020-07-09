package com.africastalking.voice;

import com.google.gson.Gson;

public final class CallEntry {
    public String status;
    public String phoneNumber;
    public String sessionId;
    public String queueName = null;
    public int numCalls = 1;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
