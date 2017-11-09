package com.africastalking;

import com.africastalking.payment.recipient.Bank;
import com.africastalking.payment.response.B2BResponse;
import com.africastalking.payment.response.B2CResponse;
import com.africastalking.payment.BankAccount;
import com.africastalking.payment.response.BankTransferResponse;
import com.africastalking.payment.response.CheckoutValidateResponse;
import com.africastalking.payment.response.CheckoutResponse;
import com.africastalking.payment.PaymentCard;
import com.africastalking.payment.recipient.Business;
import com.africastalking.payment.recipient.Consumer;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PaymentService extends Service {

    private PaymentService sInstance;
    private IPayment payment;


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

    private HashMap<String, Object> makeCheckoutValidationRequest(String transactionId, String otp) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", mUsername);
        body.put("transactionId", transactionId);
        body.put("otp", otp);
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
     * Initiate a mobile checkout.
     * @param productName Payment product used to initiate transaction
     * @param phoneNumber Phone number (in international format) of the mobile subscriber that will complete this transaction.
     * @param amount Amount to transact, along with the currency code. e.g. KES 345
     * @param metadata Optional map of any metadata that you may want to associate with this transaction.
     *                 This map will be included in the payment notification callback
     * @return {@link com.africastalking.payment.response.CheckoutResponse CheckoutResponse}
     * @throws IOException
     */
    public CheckoutResponse mobileCheckout(String productName, String phoneNumber, String amount, Map metadata) throws IOException {
        HashMap<String, Object> body = makeCheckoutRequest(productName, phoneNumber, amount, metadata);
        Call<CheckoutResponse> call = payment.mobileCheckout(body);
        Response<CheckoutResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.message());
        }
        return resp.body();
    }

    public void mobileCheckout(String productName, String phoneNumber, String amount, Map metadata, Callback<CheckoutResponse> callback) {
        HashMap<String, Object> body = makeCheckoutRequest(productName, phoneNumber, amount, metadata);
        Call<CheckoutResponse> call = payment.mobileCheckout(body);
        call.enqueue(makeCallback(callback));
    }


    /**
     * Initiate card checkout
     * @param productName Payment product used to initiate transaction
     * @param amount Amount to transact, along with the currency code. e.g. NGN 5903
     * @param paymentCard Payment card details. @see {@link com.africastalking.payment.PaymentCard PaymentCard}
     * @param narration A short description of the transaction that can be displayed on the client's statement
     * @param metadata Optional map of any metadata that you may want to associate with this transaction.
     *                 This map will be included in the payment notification callback
     * @return {@link com.africastalking.payment.response.CheckoutResponse CheckoutResponse}
     * @throws IOException
     */
    public CheckoutResponse cardCheckout(String productName, String amount, PaymentCard paymentCard, String narration, Map metadata) throws IOException {
        HashMap<String, Object> body = makeCheckoutRequest(productName, null, amount, metadata);
        body.remove("phoneNumber");
        body.put("paymentCard", paymentCard);
        body.put("narration", narration);
        Call<CheckoutResponse> call = payment.cardCheckoutCharge(body);
        Response<CheckoutResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.message());
        }
        return resp.body();
    }

    public void cardCheckout(String productName, String amount, PaymentCard cardDetails, String narration, Map metadata, Callback<CheckoutResponse> callback) {
        HashMap<String, Object> body = makeCheckoutRequest(productName, null, amount, metadata);
        body.remove("phoneNumber");
        body.put("paymentCard", cardDetails);
        body.put("narration", narration);
        Call<CheckoutResponse> call = payment.cardCheckoutCharge(body);
        call.enqueue(makeCallback(callback));
    }

    /**
     * Initiate card checkout with a checkout token
     * @param productName Payment product used to initiate transaction
     * @param amount Amount to transact, along with the currency code. e.g. NGN 5903
     * @param checkoutToken A checkout token
     * @param narration A short description of the transaction that can be displayed on the client's statement
     * @param metadata Optional map of any metadata that you may want to associate with this transaction.
     *                 This map will be included in the payment notification callback
     * @return {@link com.africastalking.payment.response.CheckoutResponse CheckoutResponse}
     * @throws IOException
     */
    public CheckoutResponse cardCheckout(String productName, String amount, String checkoutToken, String narration, Map metadata) throws IOException {
        HashMap<String, Object> body = makeCheckoutRequest(productName, null, amount, metadata);
        body.remove("phoneNumber");
        body.put("checkoutToken", checkoutToken);
        body.put("narration", narration);
        Call<CheckoutResponse> call = payment.cardCheckoutCharge(body);
        Response<CheckoutResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.message());
        }
        return resp.body();
    }

    public void cardCheckout(String productName, String amount, String checkoutToken, String narration, Map metadata, Callback<CheckoutResponse> callback) {
        HashMap<String, Object> body = makeCheckoutRequest(productName, null, amount, metadata);
        body.remove("phoneNumber");
        body.put("checkoutToken", checkoutToken);
        body.put("narration", narration);
        Call<CheckoutResponse> call = payment.cardCheckoutCharge(body);
        call.enqueue(makeCallback(callback));
    }

    /**
     * Validate a card checkout
     * @param transactionId Transaction ID
     * @param otp One-time-password from financial institution
     * @return {@link com.africastalking.payment.response.CheckoutValidateResponse CheckoutValidateResponse}
     * @throws IOException
     */
    public CheckoutValidateResponse validateCardCheckout(String transactionId, String otp) throws IOException {
        HashMap<String, Object> body = makeCheckoutValidationRequest(transactionId, otp);
        Call<CheckoutValidateResponse> call = payment.cardCheckoutValidate(body);
        Response<CheckoutValidateResponse> res = call.execute();
        if (!res.isSuccessful()) {
            throw new IOException(res.message());
        }
        return res.body();
    }

    public void validateCardCheckout(String transactionId, String otp, Callback<CheckoutValidateResponse> callback) {
        HashMap<String, Object> body = makeCheckoutValidationRequest(transactionId, otp);
        Call<CheckoutValidateResponse> call = payment.cardCheckoutValidate(body);
        call.enqueue(makeCallback(callback));
    }


    /**
     * Initiate a bank checkout
     * @param productName Payment product used to initiate transaction
     * @param amount Amount to transact, along with the currency code. e.g. NGN 5903
     * @param bankAccount A checkout token
     * @param narration A short description of the transaction that can be displayed on the client's statement
     * @param metadata Optional map of any metadata that you may want to associate with this transaction.
     *                 This map will be included in the payment notification callback
     * @return {@link com.africastalking.payment.response.CheckoutResponse CheckoutResponse}
     * @throws IOException
     */
    public CheckoutResponse bankCheckout(String productName, String amount, BankAccount bankAccount, String narration, Map metadata) throws IOException {
        HashMap<String, Object> body = makeCheckoutRequest(productName, null, amount, metadata);
        body.remove("phoneNumber");
        body.put("bankAccount", bankAccount);
        body.put("narration", narration);
        Call<CheckoutResponse> call = payment.bankCheckoutCharge(body);
        Response<CheckoutResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.message());
        }
        return resp.body();
    }

    public void bankCheckout(String productName, String amount, BankAccount bankAccount, String narration, Map metadata, Callback<CheckoutResponse> callback) {
        HashMap<String, Object> body = makeCheckoutRequest(productName, null, amount, metadata);
        body.remove("phoneNumber");
        body.put("bankAccount", bankAccount);
        body.put("narration", narration);
        Call<CheckoutResponse> call = payment.bankCheckoutCharge(body);
        call.enqueue(makeCallback(callback));
    }


    /**
     * Validate a bank checkout
     * @param transactionId Transaction ID
     * @param otp One-time-password from financial institution
     * @return {@link com.africastalking.payment.response.CheckoutValidateResponse CheckoutValidateResponse}
     * @throws IOException
     */
    public CheckoutValidateResponse validateBankCheckout(String transactionId, String otp) throws IOException {
        HashMap<String, Object> body = makeCheckoutValidationRequest(transactionId, otp);
        Call<CheckoutValidateResponse> call = payment.bankCheckoutValidate(body);
        Response<CheckoutValidateResponse> res = call.execute();
        if (!res.isSuccessful()) {
            throw new IOException(res.message());
        }
        return res.body();
    }

    public void validateBankCheckout(String transactionId, String otp, Callback<CheckoutValidateResponse> callback) {
        HashMap<String, Object> body = makeCheckoutValidationRequest(transactionId, otp);
        Call<CheckoutValidateResponse> call = payment.bankCheckoutValidate(body);
        call.enqueue(makeCallback(callback));
    }


    /**
     * Initiate bank transfer
     * @param productName
     * @param recipients
     * @return
     * @throws IOException
     */
    public BankTransferResponse bankTransfer(String productName, List<Bank> recipients) throws IOException {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", mUsername);
        body.put("productName", productName);
        body.put("recipients", recipients);
        Call<BankTransferResponse> call = payment.bankTransfer(body);
        Response<BankTransferResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.message());
        }
        return resp.body();
    }

    public void bankTransfer(String productName, List<Bank> recipients, Callback<BankTransferResponse> callback) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", mUsername);
        body.put("productName", productName);
        body.put("recipients", recipients);
        Call<BankTransferResponse> call = payment.bankTransfer(body);
        call.enqueue(makeCallback(callback));
    }

    /**
     * Make a B2C request
     * @param product Payment product used to initiate transaction
     * @param recipients {@link com.africastalking.payment.recipient.Consumer Consumers} recipients of the transaction
     * @return {@com}
     */
    public B2CResponse payConsumers(String product, List<Consumer> recipients) throws IOException {
        HashMap<String, Object> body = makeB2CRequest(product, recipients);
        Call<B2CResponse> call = payment.requestB2C(body);
        Response<B2CResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.message());
        }
        return resp.body();
    }

    public void payConsumers(String product, List<Consumer> recipients, Callback<B2CResponse> callback) {
        HashMap<String, Object> body = makeB2CRequest(product, recipients);
        Call<B2CResponse> call = payment.requestB2C(body);
        call.enqueue(makeCallback(callback));
    }


    /**
     * Make a B2B request
     * @param product Payment product used to initiate transaction
     * @param recipient {@link com.africastalking.payment.recipient.Business Business} recipient of the transaction
     * @return {@link com.africastalking.payment.response.B2BResponse B2BResponse}
     * @throws IOException
     */
    public B2BResponse payBusiness(String product, Business recipient) throws IOException {
        HashMap<String, Object> body = makeB2BRequest(product, recipient);
        Call<B2BResponse> call = payment.requestB2B(body);
        Response<B2BResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.message());
        }
        return resp.body();
    }

    public void payBusiness(String product, Business recipient, Callback<B2BResponse> callback) {
        HashMap<String, Object> body = makeB2BRequest(product, recipient);
        Call<B2BResponse> call = payment.requestB2B(body);
        call.enqueue(makeCallback(callback));
    }

}
