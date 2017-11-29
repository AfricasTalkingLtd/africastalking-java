package com.africastalking;

import com.africastalking.token.AuthTokenResponse;
import com.africastalking.token.CheckoutTokenResponse;

import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public final class TokenService extends Service {

    private TokenService sInstance;
    private IToken service;


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


    public CheckoutTokenResponse createCheckoutToken(String phoneNumber) throws IOException {
        Response<CheckoutTokenResponse> resp = service.createCheckoutToken(phoneNumber).execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    public void createCheckoutToken(String phoneNumber, Callback<CheckoutTokenResponse> callback) {
        service.createCheckoutToken(phoneNumber).enqueue(makeCallback(callback));
    }

    public AuthTokenResponse generateAuthToken() throws IOException {
        Response<AuthTokenResponse> resp = service.generateAuthToken(_makeAuthToknRequestBody()).execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    public void generateAuthToken(Callback<AuthTokenResponse> callback) {
        service.generateAuthToken(_makeAuthToknRequestBody()).enqueue(makeCallback(callback));
    }

    private String _makeAuthToknRequestBody() {
        return "{\"username\": \""+ mUsername + "\"}";
    }
}