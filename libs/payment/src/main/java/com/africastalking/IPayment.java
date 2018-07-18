package com.africastalking;


import com.africastalking.payment.response.*;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import java.util.HashMap;

interface IPayment {
    @POST("mobile/b2c/request")
    Call<B2CResponse> requestB2C(@Body HashMap<String, Object> body);

    @POST("mobile/b2b/request")
    Call<B2BResponse> requestB2B(@Body HashMap<String, Object> body);

    @POST("mobile/checkout/request")
    Call<CheckoutResponse> mobileCheckout(@Body HashMap<String, Object> body);

    @POST("card/checkout/charge")
    Call<CheckoutResponse> cardCheckoutCharge(@Body HashMap<String, Object> body);

    @POST("card/checkout/validate")
    Call<CheckoutValidateResponse> cardCheckoutValidate(@Body HashMap<String, Object> body);

    @POST("bank/checkout/charge")
    Call<CheckoutResponse> bankCheckoutCharge(@Body HashMap<String, Object> body);

    @POST("bank/checkout/validate")
    Call<CheckoutValidateResponse> bankCheckoutValidate(@Body HashMap<String, Object> body);

    @POST("bank/transfer")
    Call<BankTransferResponse> bankTransfer(@Body HashMap<String, Object> body);

    @POST("transfer/wallet")
    Call<WalletTransferResponse> walletTransfer(@Body HashMap<String, Object> body);

    @POST("topup/stash")
    Call<TopupStashResponse> topupStash(@Body HashMap<String, Object> body);

    @GET("/query/transaction/fetch")
    Call<FetchTransactionsResponse> fetchTransactions(@QueryMap HashMap<String, String> query);

    @GET("/query/transaction/find")
    Call<FindTransactionResponse> findTransaction(@QueryMap HashMap<String, String> query);
}
