package com.africastalking.token;


import com.google.gson.Gson;

public final class AuthTokenResponse {
    public String token;
    public long lifetimeInSeconds;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
