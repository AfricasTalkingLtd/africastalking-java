package com.africastalking.chat;

import com.google.gson.Gson;

final public class ChatResponse {
    public String messageId;
    public String status;
    public String phoneNumber;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
