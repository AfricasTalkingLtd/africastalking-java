package com.africastalking;

import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

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
        if (resp.isSuccessful()) {
            return resp.body();
        }
        throw new IOException(resp.message());
    }

    public void createCheckoutToken(String phoneNumber, Callback<String> callback) {
        service.createCheckoutToken(phoneNumber).enqueue(makeCallback(callback));
    }

    public String generateAuthToken() throws IOException {
        Response<String> resp = service.generateAuthToken(_makeAuthToknRequestBody()).execute();
        if (resp.isSuccessful()) {
            return resp.body();
        }
        throw new IOException(resp.message());
    }

    public void generateAuthToken(Callback<String> callback) {
        service.generateAuthToken(_makeAuthToknRequestBody()).enqueue(makeCallback(callback));
    }

    private String _makeAuthToknRequestBody() {
        return "{\"username\": \""+ mUsername + "\"}";
    }
}