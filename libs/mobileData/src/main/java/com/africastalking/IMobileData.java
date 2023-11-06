package com.africastalking;


import com.africastalking.mobileData.response.*;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import java.util.HashMap;

interface IMobileData {
    @POST("mobile/data/request")
    Call<MobileDataResponse> requestMobileData(@Body HashMap<String, Object> body);

    @GET("/query/transaction/find")
    Call<FindTransactionResponse> findTransaction(@QueryMap HashMap<String, String> query);

    @GET("/query/wallet/balance")
    Call<WalletBalanceResponse> fetchWalletBalance(@QueryMap HashMap<String, String> query);
}
