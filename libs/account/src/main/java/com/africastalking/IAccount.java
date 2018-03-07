package com.africastalking;

import com.africastalking.account.AccountResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Account Endpoints
 */
interface IAccount {
    @GET("user")
    Call<AccountResponse> fetchAccount(@Query("username") String username);
}
