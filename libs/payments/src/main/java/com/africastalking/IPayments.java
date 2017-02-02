package com.africastalking;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.util.HashMap;

interface IPayments {

    @POST("mobile/checkout/request")
    Call<String> checkout(@Body HashMap<String, Object> body);

}
