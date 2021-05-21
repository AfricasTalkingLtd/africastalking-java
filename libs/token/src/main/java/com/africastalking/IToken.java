package com.africastalking;

import com.africastalking.token.AuthTokenResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

interface IToken {

    @Headers("Content-Type: application/json")
    @POST("auth-token/generate")
    Call<AuthTokenResponse> generateAuthToken(@Body String body);
}
