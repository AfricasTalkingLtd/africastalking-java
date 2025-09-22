package com.africastalking.chat;

import com.google.gson.Gson;

public abstract class MessageBody {
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
