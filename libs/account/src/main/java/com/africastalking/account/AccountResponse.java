package com.africastalking.account;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public final class AccountResponse {

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
