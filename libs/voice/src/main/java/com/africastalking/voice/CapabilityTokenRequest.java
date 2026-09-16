package com.africastalking.voice;

import com.google.gson.Gson;

public class CapabilityTokenRequest {
    public String username;
    public String clientName;
    public String phoneNumber;
    public String incoming = "true";
    public String outgoing = "true";
    public String expire = "86400s";

    public CapabilityTokenRequest() {}

    public CapabilityTokenRequest(String username, String clientName, String phoneNumber) {
        this.username = username;
        this.clientName = clientName;
        this.phoneNumber = phoneNumber;
    }

    public CapabilityTokenRequest(String username, String clientName, String phoneNumber, boolean incoming, boolean outgoing, String expire) {
        this.username = username;
        this.clientName = clientName;
        this.phoneNumber = phoneNumber;
        this.incoming = String.valueOf(incoming);
        this.outgoing = String.valueOf(outgoing);
        this.expire = expire;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
