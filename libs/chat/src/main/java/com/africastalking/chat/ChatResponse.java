package com.africastalking.chat;

import com.google.gson.Gson;

final public class ChatResponse {
    public String customerId;
    public String messageId;
    public String status;
    public int statusCode;
    public String description;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
