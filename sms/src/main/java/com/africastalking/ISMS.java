package com.africastalking;


import retrofit2.Call;
import retrofit2.http.*;

public interface ISMS {


    @FormUrlEncoded
    @POST("messaging")
    Call<String> send(@Field("username") String username, @Field("to") String to,
                      @Field("from") String from, @Field("message") String message);


    @FormUrlEncoded
    @POST("messaging")
    Call<String> sendBulk(@Field("username") String username, @Field("to") String to,
                          @Field("from") String from, @Field("message") String message,
                          @Field("bulkSMSMode") int bulkMode, @Field("enqueue") String enqueue);

    @FormUrlEncoded
    @POST("messaging")
    Call<String> sendPremium(@Field("username") String username, @Field("to") String to,
                             @Field("from") String from, @Field("message") String message,
                             @Field("keyword") String keyword, @Field("linkId") String linkId,
                             @Field("retryDurationInHours") String retryDurationInHours);

    @GET("messaging")
    Call<String> fetchMessage(@Query("username") String username, @Query("lastReceivedId") String lastReceivedId);


    @GET("subscription")
    Call<String> fetchSubsciption(@Query("username") String username, @Query("shortCode") String shortCode,
                                  @Query("keyword") String keyword, @Query("lastReceivedId") String lastReceivedId);

    @FormUrlEncoded
    @POST("subscription/create")
    Call<String> createSubscription(@Field("username") String username, @Field("shortCode") String shortCode,
                                    @Field("keyword") String keyword, @Field("phoneNumber") String phoneNumber);


}
