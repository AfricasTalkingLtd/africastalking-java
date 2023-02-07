package com.africastalking;


import com.africastalking.airtime.AirtimeResponse;

import retrofit2.Call;
import retrofit2.http.*;

interface IAirtime {

    @FormUrlEncoded
    @POST("send")
    Call<AirtimeResponse> send(@Field("username") String username, @Field("recipients") String recipients);

}
