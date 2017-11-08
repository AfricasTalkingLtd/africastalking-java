package com.africastalking;

import com.africastalking.token.AuthTokenResponse;
import com.africastalking.token.CheckoutTokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Body;

interface IToken {

    @FormUrlEncoded
    @POST("checkout/token/create")
    Call<CheckoutTokenResponse> createCheckoutToken(@Field("phoneNumber") String phoneNumber);

    @Headers("Content-Type: application/json")
    @POST("auth-token/generate")
    Call<AuthTokenResponse> generateAuthToken(@Body String body);
}
