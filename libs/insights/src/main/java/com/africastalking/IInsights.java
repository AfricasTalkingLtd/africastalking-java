package com.africastalking;

import com.africastalking.insights.SimSwapResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

interface IInsights {

    @Headers("Content-Type: application/json")
    @POST("v1/sim-swap")
    Call<SimSwapResponse> checkSimSwapState(@Body String body);
}
