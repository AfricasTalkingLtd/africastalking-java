package com.africastalking.example.models;


import com.google.gson.Gson;

public class RequestSend {
    public String to, message;

    public static RequestSend parse(String json) {
        return new Gson().fromJson(json, RequestSend.class);
    }

}
