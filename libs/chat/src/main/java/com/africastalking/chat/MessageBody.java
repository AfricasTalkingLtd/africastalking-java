package com.africastalking.chat;

import com.google.gson.Gson;

public abstract class MessageBody {
    protected String type;

    protected MessageBody(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
