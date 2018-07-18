package com.africastalking.payment;

import java.util.HashMap;

import com.google.gson.Gson;


public final class Transaction {
    public String value;
    public String source;
    public String status;
    public String provider;
    public String category;
    public String sourceType;
    public String productName;
    public String destination;
    public String description;
    public String creationTime;
    public String providerRefId;
    public String transactionId;
    public String transactionFee;
    public String providerChannel;
    public String destinationType;
    public String transactionDate;
    public HashMap<String, String> requestMetadata;
    public HashMap<String, String> providerMetadata;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
