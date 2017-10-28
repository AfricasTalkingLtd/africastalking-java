package com.africastalking;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.HashMap;

interface IPayment {
    @POST("mobile/b2c/request")
    Call<String> requestB2C(@Body HashMap<String, Object> body);

    @POST("mobile/b2b/request")
    Call<String> requestB2B(@Body HashMap<String, Object> body);

    @POST("mobile/checkout/request")
    Call<String> mobileCheckout(@Body HashMap<String, Object> body);

    @POST("card/checkout/charge")
    Call<String> cardCheckoutCharge(@Body HashMap<String, Object> body);

    @POST("card/checkout/validate")
    Call<String> cardCheckoutValidate(@Body HashMap<String, Object> body);

    @POST("bank/checkout/charge")
    Call<String> bankCheckoutCharge(@Body HashMap<String, Object> body);

    @POST("bank/checkout/validate")
    Call<String> bankCheckoutValidate(@Body HashMap<String, Object> body);
}
