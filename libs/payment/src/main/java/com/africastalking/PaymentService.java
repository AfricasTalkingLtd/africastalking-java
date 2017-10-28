package com.africastalking;

import com.africastalking.payments.BankAccount;
import com.africastalking.payments.PaymentCard;
import com.africastalking.payments.recipient.Business;
import com.africastalking.payments.recipient.Consumer;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
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

    private HashMap<String, Object> makeCheckoutValidationRequest(String transactionId, String token) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", mUsername);
        body.put("transactionId", transactionId);
        body.put("token", token);
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
    public String mobileCheckout(String productName, String phoneNumber, String amount, Map metadata) throws IOException {
        HashMap<String, Object> body = makeCheckoutRequest(productName, phoneNumber, amount, metadata);
        Call<String> call = payment.mobileCheckout(body);
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
     * @param amount String e.g. KES 785
     * @param metadata
     * @param callback
     */
    public void mobileCheckout(String productName, String phoneNumber, String amount, Map metadata, Callback<String> callback) {
        HashMap<String, Object> body = makeCheckoutRequest(productName, phoneNumber, amount, metadata);
        Call<String> call = payment.mobileCheckout(body);
        call.enqueue(makeCallback(callback));
    }

    /**
     * @param productName
     */
    public String cardCheckout(String productName, String amount, PaymentCard cardDetails, Map metadata) throws IOException {
        HashMap<String, Object> body = makeCheckoutRequest(productName, null, amount, metadata);
        body.remove("phoneNumber");
        body.put("paymentCard", cardDetails);
        Call<String> call = payment.cardCheckoutCharge(body);
        Response<String> resp = call.execute();
        if (!resp.isSuccessful()) {
            return resp.message();
        }
        return resp.body();
    }

    public void cardCheckout(String productName, String amount, PaymentCard cardDetails, Map metadata, Callback<String> callback) {
        HashMap<String, Object> body = makeCheckoutRequest(productName, null, amount, metadata);
        body.remove("phoneNumber");
        body.put("paymentCard", cardDetails);
        Call<String> call = payment.cardCheckoutCharge(body);
        call.enqueue(makeCallback(callback));
    }

    public String bankCheckout(String productName, String amount, BankAccount bankAccount, Map metadata) throws IOException {
        HashMap<String, Object> body = makeCheckoutRequest(productName, null, amount, metadata);
        body.remove("phoneNumber");
        body.put("bankAccount", bankAccount);
        Call<String> call = payment.bankCheckoutCharge(body);
        Response<String> resp = call.execute();
        if (!resp.isSuccessful()) {
            return resp.message();
        }
        return resp.body();
    }

    public void bankCheckout(String productName, String amount, BankAccount bankAccount, Map metadata, Callback<String> callback) {
        HashMap<String, Object> body = makeCheckoutRequest(productName, null, amount, metadata);
        body.remove("phoneNumber");
        body.put("bankAccount", bankAccount);
        Call<String> call = payment.bankCheckoutCharge(body);
        call.enqueue(makeCallback(callback));
    }

    public String validateCardCheckout(String transactionId, String token) throws IOException {
        HashMap<String, Object> body = makeCheckoutValidationRequest(transactionId, token);
        Call<String> call = payment.cardCheckoutValidate(body);
        Response<String> res = call.execute();
        if (!res.isSuccessful()) {
            return res.message();
        }
        return res.body();
    }

    public void validateCardCheckout(String transactionId, String token, Callback<String> callback) {
        HashMap<String, Object> body = makeCheckoutValidationRequest(transactionId, token);
        Call<String> call = payment.cardCheckoutValidate(body);
        call.enqueue(makeCallback(callback));
    }

    public String validateBankCheckout(String transactionId, String token) throws IOException {
        HashMap<String, Object> body = makeCheckoutValidationRequest(transactionId, token);
        Call<String> call = payment.bankCheckoutValidate(body);
        Response<String> res = call.execute();
        if (!res.isSuccessful()) {
            return res.message();
        }
        return res.body();
    }

    public void validateBankCheckout(String transactionId, String token, Callback<String> callback) {
        HashMap<String, Object> body = makeCheckoutValidationRequest(transactionId, token);
        Call<String> call = payment.bankCheckoutValidate(body);
        call.enqueue(makeCallback(callback));
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
