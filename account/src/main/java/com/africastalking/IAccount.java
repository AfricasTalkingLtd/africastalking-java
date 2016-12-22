package com.africastalking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Account Endpoints
 */
interface IAccount {
    @GET("user")
    Call<String> getUser(@Query("username") String username);
}
