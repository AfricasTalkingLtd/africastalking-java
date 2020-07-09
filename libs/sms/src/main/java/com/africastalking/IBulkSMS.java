package com.africastalking;


import com.africastalking.sms.FetchMessageResponse;
import com.africastalking.sms.FetchSubscriptionResponse;
import com.africastalking.sms.SendMessageResponse;
import com.africastalking.sms.SubscriptionResponse;

import retrofit2.Call;
import retrofit2.http.*;

interface IBulkSMS {

    @FormUrlEncoded
    @POST("messaging")
    Call<SendMessageResponse> send(@Field("username") String username, @Field("to") String to,
                                       @Field("from") String from, @Field("message") String message,
                                       @Field("bulkSMSMode") int bulkMode, @Field("enqueue") String enqueue);

    @GET("messaging")
    Call<FetchMessageResponse> fetchMessages(@Query("username") String username, @Query("lastReceivedId") long lastReceivedId);

}
