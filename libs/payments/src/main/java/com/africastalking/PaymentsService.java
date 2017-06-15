package com.africastalking;

import com.africastalking.recipient.Business;
import com.africastalking.recipient.Consumer;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentsService extends Service {

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

    private HashMap<String, Object> makeB2CRequest(String product, List<Consumer> recipients) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", mUsername);
        body.put("productName", product);
        body.put("recipients", recipients);

        return body;
    }

    private HashMap<String, Object> makeB2BRequest(String product, Business recipient) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", mUsername);
        body.put("productName", product);

        //
        Gson gson = new Gson();
        String json = gson.toJson(recipient);
        HashMap map = gson.fromJson(json, HashMap.class);
        body.putAll(map);

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

    /**
     *
     * @param productName
     * @param phoneNumber
     * @param amount
     * @param currency
     * @return
     * @throws IOException
     */
    public String checkout(String productName, String phoneNumber, float amount, Currency currency) throws IOException {
        return this.checkout(productName, phoneNumber, amount, currency, new HashMap());
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
        this.checkout(productName, phoneNumber, amount, currency, new HashMap(), callback);
    }


    /**
     *
     * @param product
     * @param recipients
     * @return
     */
    public String payConsumers(String product, List<Consumer> recipients) throws IOException {
        HashMap<String, Object> body = makeB2CRequest(product, recipients);
        Call<String> call = payment.requestB2C(body);
        Response<String> res = call.execute();
        return res.body();
    }

    public String payConsumer(String product, Consumer recipient) throws IOException {
        List<Consumer> recipients = new ArrayList<>();
        recipients.add(recipient);
        return this.payConsumers(product, recipients);
    }

    /**
     *
     * @param product
     * @param recipients
     * @param callback
     */
    public void payConsumers(String product, List<Consumer> recipients, Callback<String> callback) {
        HashMap<String, Object> body = makeB2CRequest(product, recipients);
        Call<String> call = payment.requestB2C(body);
        call.enqueue(makeCallback(callback));
    }

    public void payConsumer(String product, Consumer recipient, Callback<String> callback) {
        List<Consumer> recipients = new ArrayList<>();
        recipients.add(recipient);
        this.payConsumers(product, recipients, callback);
    }

    public String payBusiness(String product, Business recipient) throws IOException {
        HashMap<String, Object> body = makeB2BRequest(product, recipient);
        Call<String> call = payment.requestB2B(body);
        Response<String> res = call.execute();
        return res.body();
    }

    public void payBusiness(String product, Business recipient, Callback<String> callback) {
        HashMap<String, Object> body = makeB2BRequest(product, recipient);
        Call<String> call = payment.requestB2B(body);
        call.enqueue(makeCallback(callback));
    }



}
