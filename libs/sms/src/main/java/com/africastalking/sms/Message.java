package com.africastalking.sms;


import com.google.gson.Gson;

public final class Message {
    public String from;
    public String to;
    public String text;
    public String linkId;
    public String date;
    public long id;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
