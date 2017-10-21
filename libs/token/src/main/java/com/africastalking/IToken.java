package com.africastalking;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Body;

interface IToken {

    @FormUrlEncoded
    @POST("checkout/token/create")
    Call<String> createCheckoutToken(@Field("phoneNumber") String phoneNumber);

    @POST("auth-token/generate")
    Call<String> generateAuthToken(@Body String body);
}
