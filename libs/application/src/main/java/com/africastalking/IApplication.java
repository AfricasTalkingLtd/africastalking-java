package com.africastalking;

import com.africastalking.application.ApplicationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Account Endpoints
 */
interface IApplication {
    @GET("user")
    Call<ApplicationResponse> fetchApplicationData(@Query("username") String username);
}
