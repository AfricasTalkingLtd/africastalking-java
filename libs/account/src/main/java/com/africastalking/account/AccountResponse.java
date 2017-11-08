package com.africastalking.account;


import com.google.gson.annotations.SerializedName;

public final class AccountResponse {

    @SerializedName("UserData")
    public UserData userData;

    public static final class UserData {
        public String balance;
    }
}
