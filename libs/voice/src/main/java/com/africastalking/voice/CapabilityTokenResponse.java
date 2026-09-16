package com.africastalking.voice;

import com.google.gson.Gson;

public class CapabilityTokenResponse {
    public String clientName;
    public boolean incoming;
    public boolean outgoing;
    public String lifeTimeSec;
    public String token;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
