package com.africastalking;

import com.africastalking.payments.recipient.Business;
import com.africastalking.payments.recipient.Consumer;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenService extends Service {

    TokenService sInstance;
    IToken service;


    private TokenService(String username, String apiKey) {
        super(username, apiKey);

    }

    TokenService() {
        super();
    }

    @Override
    protected TokenService getInstance(String username, String apiKey) {
        if(sInstance == null) {
            sInstance = new TokenService(username, apiKey);
        }
        return sInstance;
    }

    @Override
    protected boolean isInitialized() {
        return sInstance != null;
    }

    @Override
    protected void initService() {
        String baseUrl = "https://api."+ (isSandbox ? Const.SANDBOX_DOMAIN : Const.PRODUCTION_DOMAIN) + "/";
        service = mRetrofitBuilder
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
                .create(IToken.class);
    }

    @Override
    protected void destroyService() {
        if (sInstance != null) {
            sInstance = null;
        }
    }


    public String createCheckoutToken(String phoneNumber) throws IOException {
        Response<String> resp = service.createCheckoutToken(phoneNumber).execute();
        return resp.body();
    }

    public void createCheckoutToken(String phoneNumber, Callback<String> callback) {
        service.createCheckoutToken(phoneNumber).enqueue(makeCallback(callback));
    }
}