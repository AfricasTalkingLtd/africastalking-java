package com.africastalking.chat;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

final public class ChatMessage {
    public String username;
    public String productId;
    public String customerNumber;
    public String channel;
    public String channelNumber;

    public MessageBody body;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
