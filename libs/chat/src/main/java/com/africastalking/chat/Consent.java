package com.africastalking.chat;

import com.google.gson.Gson;

public final class Consent {
    public String username;
    public String customerNumber;
    public String channel;          // 'Whatsapp'
    public String channelNumber;
    public String action;           // 'OptIn' 'OptOut'

    public Consent(String username, String customerNumber, String channel, String channelNumber, String action) {
        this.username = username;
        this.customerNumber = customerNumber;
        this.channel = channel;
        this.channelNumber = channelNumber;
        this.action = action;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
