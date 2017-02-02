package com.africastalking;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class PaymentsService extends Service {

    PaymentsService sInstance;
    IPayments payment;



    private PaymentsService(String username, String apiKey) {
        super(username, apiKey, Format.JSON, Currency.KES);

    }

    PaymentsService() {
        super();
    }

    @Override
    protected PaymentsService getInstance(String username, String apiKey, Format format, Currency currency) {
        if(sInstance == null) {
            sInstance = new PaymentsService(username, apiKey);
        }
        return sInstance;
    }

    @Override
    protected boolean isInitialized() {
        return sInstance != null;
    }

    @Override
    protected void initService() {
        String baseUrl = "https://payments."+ (AfricasTalking.ENV == Environment.SANDBOX ? Const.SANDBOX_DOMAIN : Const.PRODUCTION_DOMAIN) + "/";
        payment = mRetrofitBuilder
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
                .create(IPayments.class);
    }

    @Override
    protected void destroyService() {
        if (sInstance != null) {
            sInstance = null;
        }
    }


    private HashMap<String, Object> makeCheckoutRequest(String product, String phone, float amount, Currency currency, Map metadata) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", mUsername);
        body.put("productName", product);
        body.put("phoneNumber", phone);
        body.put("amount", amount);
        body.put("currencyCode", currency.toString());
        body.put("metadata", metadata);
        return body;
    }

    /**
     *
     * @param productName
     * @param phoneNumber
     * @param amount
     * @param currency
     * @param metadata
     * @return
     * @throws IOException
     */
    public String checkout(String productName, String phoneNumber, float amount, Currency currency, Map metadata) throws IOException {

        HashMap<String, Object> body = makeCheckoutRequest(productName, phoneNumber, amount, currency, metadata);

        Call<String> call = payment.checkout(body);
        Response<String> res = call.execute();
        return res.body();

    }

    public String checkout(String productName, String phoneNumber, float amount, Currency currency) throws IOException {

        HashMap<String, Object> body = makeCheckoutRequest(productName, phoneNumber, amount, currency, null);

        Call<String> call = payment.checkout(body);
        Response<String> res = call.execute();
        return res.body();

    }

    /**
     *
     * @param productName
     * @param phoneNumber
     * @param amount
     * @param currency
     * @param metadata
     * @param callback
     */
    public void checkout(String productName, String phoneNumber, float amount, Currency currency, Map metadata, Callback<String> callback) {
        HashMap<String, Object> body = makeCheckoutRequest(productName, phoneNumber, amount, currency, metadata);
        Call<String> call = payment.checkout(body);
        call.enqueue(makeCallback(callback));
    }

    public void checkout(String productName, String phoneNumber, float amount, Currency currency, Callback<String> callback) {
        HashMap<String, Object> body = makeCheckoutRequest(productName, phoneNumber, amount, currency, null);
        Call<String> call = payment.checkout(body);
        call.enqueue(makeCallback(callback));
    }

    // TODO: http://docs.africastalking.com/payments/mobile-b2c
    public String transfer(String to, float amount) {
        return null;
    }

    public void transfer(String to, float amount, Callback<String> callback) {

    }

}
