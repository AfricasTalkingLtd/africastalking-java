package com.africastalking;


import com.africastalking.sms.FetchSubscriptionResponse;
import com.africastalking.sms.SendMessageResponse;
import com.africastalking.sms.SubscriptionResponse;
import retrofit2.Call;
import retrofit2.http.*;

interface IPremiumSMS {

    @FormUrlEncoded
    @POST("messaging")
    Call<SendMessageResponse> send(@Field("username") String username, @Field("to") String to,
                                          @Field("from") String from, @Field("message") String message,
                                          @Field("keyword") String keyword, @Field("linkId") String linkId,
                                          @Field("retryDurationInHours") String retryDurationInHours,
                                          @Field("bulkSMSMode") int bulkMode);

    @GET("subscription")
    Call<FetchSubscriptionResponse> fetchSubscriptions(@Query("username") String username, @Query("shortCode") String shortCode,
                                                       @Query("keyword") String keyword, @Query("lastReceivedId") long lastReceivedId);

    @FormUrlEncoded
    @POST("subscription/create")
    Call<SubscriptionResponse> createSubscription(@Field("username") String username, @Field("shortCode") String shortCode,
                                                  @Field("keyword") String keyword, @Field("phoneNumber") String phoneNumber,
                                                  @Field("checkoutToken") String checkoutToken);

    @FormUrlEncoded
    @POST("subscription/delete")
    Call<SubscriptionResponse> deleteSubscription(@Field("username") String username, @Field("shortCode") String shortCode,
                                                  @Field("keyword") String keyword, @Field("phoneNumber") String phoneNumber);
}
