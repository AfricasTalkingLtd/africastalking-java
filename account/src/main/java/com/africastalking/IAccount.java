package com.africastalking;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Account Endpoints
 */
interface IAccount {
    @GET("user")
    Call<String> fetchUser();
}
