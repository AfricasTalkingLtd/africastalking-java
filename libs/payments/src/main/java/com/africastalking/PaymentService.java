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

public class PaymentService extends Service {

    PaymentService sInstance;
    IPayment payment;


    private PaymentService(String username, String apiKey) {
        super(username, apiKey);

    }

    PaymentService() {
        super();
    }

    @Override
    protected PaymentService getInstance(String username, String apiKey) {
        if(sInstance == null) {
            sInstance = new PaymentService(username, apiKey);
        }
        return sInstance;
    }

    @Override
    protected boolean isInitialized() {
        return sInstance != null;
    }

    @Override
    protected void initService() {
        String baseUrl = "https://payments."+ (isSandbox ? Const.SANDBOX_DOMAIN : Const.PRODUCTION_DOMAIN) + "/";
        payment = mRetrofitBuilder
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
                .create(IPayment.class);
    }

    @Override
    protected void destroyService() {
        if (sInstance != null) {
            sInstance = null;
        }
    }


    private HashMap<String, Object> makeCheckoutRequest(String product, String phone, String amount, Map metadata) {
        HashMap<String, Object> body = new HashMap<>();
        String[] amountParts = amount.split(" ");
        body.put("username", mUsername);
        body.put("productName", product);
        body.put("phoneNumber", phone);
        body.put("amount", Float.parseFloat(amountParts[1]));
        body.put("currencyCode", amountParts[0]);
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
     * @param amount String e.g. UXG 745
     * @param metadata
     * @return
     * @throws IOException
     */
    public String checkout(String productName, String phoneNumber, String amount, Map metadata) throws IOException {

        HashMap<String, Object> body = makeCheckoutRequest(productName, phoneNumber, amount, metadata);

        Call<String> call = payment.checkout(body);
        Response<String> resp = call.execute();
        if (!resp.isSuccessful()) {
            return resp.message();
        }
        return resp.body();

    }

    /**
     *
     * @param productName
     * @param phoneNumber
     * @param amount String e.g. KES 6952
     * @return
     * @throws IOException
     */
    public String checkout(String productName, String phoneNumber, String amount) throws IOException {
        return this.checkout(productName, phoneNumber, amount, new HashMap());
    }

    /**
     *
     * @param productName
     * @param phoneNumber
     * @param amount String e.g. KES 785
     * @param metadata
     * @param callback
     */
    public void checkout(String productName, String phoneNumber, String amount, Map metadata, Callback<String> callback) {
        HashMap<String, Object> body = makeCheckoutRequest(productName, phoneNumber, amount, metadata);
        Call<String> call = payment.checkout(body);
        call.enqueue(makeCallback(callback));
    }

    public void checkout(String productName, String phoneNumber, String amount, Callback<String> callback) {
        this.checkout(productName, phoneNumber, amount, new HashMap(), callback);
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
        Response<String> resp = call.execute();
        if (!resp.isSuccessful()) {
            return resp.message();
        }
        return resp.body();
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
