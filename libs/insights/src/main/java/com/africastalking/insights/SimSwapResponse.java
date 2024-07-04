package com.africastalking.insights;

import com.africastalking.ATCash;
import com.google.gson.Gson;

import java.util.List;

public class SimSwapResponse {

    public String transactionId;
    public String status;
    public ATCash totalCost;
    public List<SwapResponse> responses;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
