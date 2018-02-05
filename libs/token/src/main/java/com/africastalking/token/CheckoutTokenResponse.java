package com.africastalking.token;

import com.google.gson.Gson;

public final class CheckoutTokenResponse {
    public String token;
    public String description;
    
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
