package com.africastalking.application;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public final class ApplicationResponse {

    @SerializedName("UserData")
    public UserData userData;

    public static final class UserData {
        public String balance;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
