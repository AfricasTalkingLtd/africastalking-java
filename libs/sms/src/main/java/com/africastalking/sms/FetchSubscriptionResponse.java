package com.africastalking.sms;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public final class FetchSubscriptionResponse {
    @SerializedName("Subscriptions")
    public List<Subscription> subscriptions = new ArrayList<>();

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
