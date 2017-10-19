package com.africastalking;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.HashMap;

interface IPayment {

    @POST("mobile/checkout/request")
    Call<String> checkout(@Body HashMap<String, Object> body);

    @POST("mobile/b2c/request")
    Call<String> requestB2C(@Body HashMap<String, Object> body);


    @POST("mobile/b2b/request")
    Call<String> requestB2B(@Body HashMap<String, Object> body);

}
