package com.africastalking;


import retrofit2.Call;
import retrofit2.http.*;

interface IAirtime {

    @FormUrlEncoded
    @POST("send")
    Call<String> send(@Field("username") String username, @Field(value = "recipients", encoded = false) String recipients);

}
