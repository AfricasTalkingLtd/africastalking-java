package com.africastalking;

import com.africastalking.payment.recipient.Bank;
import com.africastalking.payment.response.*;
import com.africastalking.payment.Transaction;
import com.africastalking.payment.BankAccount;
import com.africastalking.payment.PaymentCard;
import com.africastalking.payment.WalletTransaction;
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


    private HashMap<String, Object> makeCheckoutRequest(String product, String amount, String narration, Map metadata) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", mUsername);
        body.put("productName", product);

        if (narration != null) {
            body.put("narration", narration);
        }
        if (metadata != null && metadata.size() > 0) {
            body.put("metadata", metadata);
        }

        try {
            String[] currenciedAmount = amount.trim().split(" ");
            body.put("currencyCode", currenciedAmount[0]);
            body.put("amount", Float.parseFloat(currenciedAmount[1]));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

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
        checkPhoneNumber(phoneNumber);
        HashMap<String, Object> body = makeCheckoutRequest(productName, amount, null, metadata);
        body.put("phoneNumber", phoneNumber);
        Call<CheckoutResponse> call = payment.mobileCheckout(body);
        Response<CheckoutResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    public void mobileCheckout(String productName, String phoneNumber, String amount, Map metadata, Callback<CheckoutResponse> callback) {

        try {
            checkPhoneNumber(phoneNumber);

            HashMap<String, Object> body = makeCheckoutRequest(productName, amount, null, metadata);
            body.put("phoneNumber", phoneNumber);
            Call<CheckoutResponse> call = payment.mobileCheckout(body);
            call.enqueue(makeCallback(callback));
        } catch (IOException e) {
            callback.onFailure(e);
            return;
        }
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
        HashMap<String, Object> body = makeCheckoutRequest(productName, amount, narration, metadata);
        body.put("paymentCard", paymentCard);
        Call<CheckoutResponse> call = payment.cardCheckoutCharge(body);
        Response<CheckoutResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    public void cardCheckout(String productName, String amount, PaymentCard cardDetails, String narration, Map metadata, Callback<CheckoutResponse> callback) {
        HashMap<String, Object> body = makeCheckoutRequest(productName, amount, narration, metadata);
        body.put("paymentCard", cardDetails);
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
        HashMap<String, Object> body = makeCheckoutRequest(productName, amount, narration, metadata);
        body.put("checkoutToken", checkoutToken);
        Call<CheckoutResponse> call = payment.cardCheckoutCharge(body);
        Response<CheckoutResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    public void cardCheckout(String productName, String amount, String checkoutToken, String narration, Map metadata, Callback<CheckoutResponse> callback) {
        HashMap<String, Object> body = makeCheckoutRequest(productName, amount, narration, metadata);
        body.put("checkoutToken", checkoutToken);
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
            throw new IOException(res.errorBody().string());
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
        HashMap<String, Object> body = makeCheckoutRequest(productName, amount, narration, metadata);
        body.put("bankAccount", bankAccount);
        Call<CheckoutResponse> call = payment.bankCheckoutCharge(body);
        Response<CheckoutResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    public void bankCheckout(String productName, String amount, BankAccount bankAccount, String narration, Map metadata, Callback<CheckoutResponse> callback) {
        HashMap<String, Object> body = makeCheckoutRequest(productName, amount, narration, metadata);
        body.put("bankAccount", bankAccount);
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
            throw new IOException(res.errorBody().string());
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
            throw new IOException(resp.errorBody().string());
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
     *
     * @param productName
     * @param targetProductCode
     * @param amount
     * @param metadata
     * @return
     * @throws IOException
     */
    public WalletTransferResponse walletTransfer(String productName, long targetProductCode, String amount, HashMap<String, String> metadata) throws IOException {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", mUsername);
        body.put("productName", productName);
        body.put("targetProductCode", targetProductCode);
        body.put("metadata", metadata);

        try {
            String[] currenciedAmount = amount.trim().split(" ");
            body.put("currencyCode", currenciedAmount[0]);
            body.put("amount", Float.parseFloat(currenciedAmount[1]));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        Call<WalletTransferResponse> call = payment.walletTransfer(body);
        Response<WalletTransferResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    public void walletTransfer(String productName, long targetProductCode, String amount, HashMap<String, String> metadata, Callback<WalletTransferResponse> callback) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", mUsername);
        body.put("productName", productName);
        body.put("targetProductCode", targetProductCode);
        body.put("metadata", metadata);

        try {
            String[] currenciedAmount = amount.trim().split(" ");
            body.put("currencyCode", currenciedAmount[0]);
            body.put("amount", Float.parseFloat(currenciedAmount[1]));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        Call<WalletTransferResponse> call = payment.walletTransfer(body);
        call.enqueue(makeCallback(callback));
    }

    public TopupStashResponse topupStash(String productName, String amount, HashMap<String, String> metadata) throws IOException {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", mUsername);
        body.put("productName", productName);
        body.put("metadata", metadata);

        try {
            String[] currenciedAmount = amount.trim().split(" ");
            body.put("currencyCode", currenciedAmount[0]);
            body.put("amount", Float.parseFloat(currenciedAmount[1]));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        Call<TopupStashResponse> call = payment.topupStash(body);
        Response<TopupStashResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    public void topupStash(String productName, String amount, HashMap<String, String> metadata, Callback<TopupStashResponse> callback) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", mUsername);
        body.put("productName", productName);
        body.put("metadata", metadata);

        try {
            String[] currenciedAmount = amount.trim().split(" ");
            body.put("currencyCode", currenciedAmount[0]);
            body.put("amount", Float.parseFloat(currenciedAmount[1]));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        Call<TopupStashResponse> call = payment.topupStash(body);
        call.enqueue(makeCallback(callback));
    }


    /**
     * Make a B2C request
     * @param product Payment product used to initiate transaction
     * @param recipients {@link com.africastalking.payment.recipient.Consumer Consumers} recipients of the transaction
     * @return
     */
    public B2CResponse mobileB2C(String product, List<Consumer> recipients) throws IOException {

        for(Consumer consumer : recipients) { checkPhoneNumber(consumer.phoneNumber); }

        HashMap<String, Object> body = makeB2CRequest(product, recipients);
        Call<B2CResponse> call = payment.requestB2C(body);
        Response<B2CResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    public void mobileB2C(String product, List<Consumer> recipients, Callback<B2CResponse> callback) {
        try {
            for(Consumer consumer : recipients) { checkPhoneNumber(consumer.phoneNumber); }

            HashMap<String, Object> body = makeB2CRequest(product, recipients);
            Call<B2CResponse> call = payment.requestB2C(body);
            call.enqueue(makeCallback(callback));

        } catch (IOException ex) {
            callback.onFailure(ex);
        }
    }


    /**
     * Make a B2B request
     * @param product Payment product used to initiate transaction
     * @param recipient {@link com.africastalking.payment.recipient.Business Business} recipient of the transaction
     * @return {@link com.africastalking.payment.response.B2BResponse B2BResponse}
     * @throws IOException
     */
    public B2BResponse mobileB2B(String product, Business recipient) throws IOException {
        HashMap<String, Object> body = makeB2BRequest(product, recipient);
        Call<B2BResponse> call = payment.requestB2B(body);
        Response<B2BResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    public void mobileB2B(String product, Business recipient, Callback<B2BResponse> callback) {
        HashMap<String, Object> body = makeB2BRequest(product, recipient);
        Call<B2BResponse> call = payment.requestB2B(body);
        call.enqueue(makeCallback(callback));
    }



    /**
     * Fetch payment transactions
     */
    public List<Transaction> fetchProductTransactions(String product, HashMap<String, String> filters) throws IOException {
        
        filters.put("username", mUsername);
        filters.put("productName", product);
        
        if (!filters.containsKey("pageNumber")) {
            filters.put("pageNumber", "1");
        }

        if (!filters.containsKey("count")) {
            filters.put("count", "10");
        }

        Call<FetchTransactionsResponse> call = payment.fetchProductTransactions(filters);
        Response<FetchTransactionsResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }

        FetchTransactionsResponse body = resp.body();
        if (body.status.contentEquals("Failure")) {
            throw new IOException(body.errorMessage);
        }

        if (body.status.contentEquals("Throttled")) {
            throw new IOException("Too many requests: Throttled");
        }

        return body.responses;
    }

    public void fetchProductTransactions(String product, HashMap<String, String> filters, Callback<List<Transaction>> callback) {
        filters.put("username", mUsername);
        filters.put("productName", product);
        
        if (!filters.containsKey("pageNumber")) {
            filters.put("pageNumber", "1");
        }

        if (!filters.containsKey("count")) {
            filters.put("count", "10");
        }

        Call<FetchTransactionsResponse> call = payment.fetchProductTransactions(filters);
        call.enqueue(new retrofit2.Callback<FetchTransactionsResponse>() {
            @Override
            public void onResponse(Call<FetchTransactionsResponse> call, Response<FetchTransactionsResponse> response) {
                if (response.isSuccessful()) {
                    FetchTransactionsResponse body = response.body();

                    if (body.status.contentEquals("Failure")) {
                        callback.onFailure(new IOException(body.errorMessage));
                    }

                    if (body.status.contentEquals("Throttled")) {
                        callback.onFailure(new IOException("Too many requests: Throttled"));
                    }

                    callback.onSuccess(body.responses);
                } else {
                    callback.onFailure(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<FetchTransactionsResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public Transaction findTransaction(String transactionId) throws IOException {
        HashMap<String, String> query = new HashMap<String, String>();
        query.put("username", mUsername);
        query.put("transactionId", transactionId);
        Call<FindTransactionResponse> call = payment.findTransaction(query);
        Response<FindTransactionResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }

        FindTransactionResponse body = resp.body();
        if (body.status.contentEquals("Failure")) {
            throw new IOException(body.errorMessage);
        }

        if (body.status.contentEquals("Throttled")) {
            throw new IOException("Too many requests: Throttled");
        }
        
        return resp.body().data;
    }

    public void findTransaction(String transactionId, Callback<Transaction> callback) {
        HashMap<String, String> query = new HashMap<String, String>();
        query.put("username", mUsername);
        query.put("transactionId", transactionId);
        Call<FindTransactionResponse> call = payment.findTransaction(query);
        call.enqueue(new retrofit2.Callback<FindTransactionResponse>() {
            @Override
            public void onResponse(Call<FindTransactionResponse> call, Response<FindTransactionResponse> response) {
                if (response.isSuccessful()) {

                    FindTransactionResponse body = response.body();
                    if (body.status.contentEquals("Failure")) {
                        callback.onFailure(new IOException(body.errorMessage));
                    }

                    if (body.status.contentEquals("Throttled")) {
                        callback.onFailure(new IOException("Too many requests: Throttled"));
                    }

                    callback.onSuccess(body.data);
                } else {
                    callback.onFailure(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<FindTransactionResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public List<WalletTransaction> fetchWalletTransactions(HashMap<String, String> filters) throws IOException {
        
        filters.put("username", mUsername);
        
        if (!filters.containsKey("pageNumber")) {
            filters.put("pageNumber", "1");
        }

        if (!filters.containsKey("count")) {
            filters.put("count", "10");
        }

        Call<WalletTransactionsResponse> call = payment.fetchWalletTransactions(filters);
        Response<WalletTransactionsResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }

        WalletTransactionsResponse body = resp.body();
        if (body.status.contentEquals("Failure")) {
            throw new IOException(body.errorMessage);
        }

        if (body.status.contentEquals("Throttled")) {
            throw new IOException("Too many requests: Throttled");
        }

        return body.responses;
    }

    public void fetchWalletTransactions(HashMap<String, String> filters, Callback<List<WalletTransaction>> callback) {
        filters.put("username", mUsername);
        
        if (!filters.containsKey("pageNumber")) {
            filters.put("pageNumber", "1");
        }

        if (!filters.containsKey("count")) {
            filters.put("count", "10");
        }

        Call<WalletTransactionsResponse> call = payment.fetchWalletTransactions(filters);
        call.enqueue(new retrofit2.Callback<WalletTransactionsResponse>() {
            @Override
            public void onResponse(Call<WalletTransactionsResponse> call, Response<WalletTransactionsResponse> response) {
                if (response.isSuccessful()) {
                    WalletTransactionsResponse body = response.body();

                    if (body.status.contentEquals("Failure")) {
                        callback.onFailure(new IOException(body.errorMessage));
                    }

                    if (body.status.contentEquals("Throttled")) {
                        callback.onFailure(new IOException("Too many requests: Throttled"));
                    }

                    callback.onSuccess(body.responses);
                } else {
                    callback.onFailure(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<WalletTransactionsResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public WalletBalanceResponse fetchWalletBalance() throws IOException {
        HashMap<String, String> filters = new HashMap<>();
        filters.put("username", mUsername);

        Call<WalletBalanceResponse> call = payment.fetchWalletBalance(filters);
        Response<WalletBalanceResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }

        WalletBalanceResponse body = resp.body();
        if (body.status.contentEquals("Failure")) {
            throw new IOException(body.errorMessage);
        }

        if (body.status.contentEquals("Throttled")) {
            throw new IOException("Too many requests: Throttled");
        }

        return body;
    }

    public void fetchWalletBalance(Callback<WalletBalanceResponse> callback) {
        HashMap<String, String> filters = new HashMap<>();
        filters.put("username", mUsername);

        Call<WalletBalanceResponse> call = payment.fetchWalletBalance(filters);
        
        call.enqueue(new retrofit2.Callback<WalletBalanceResponse>() {
            @Override
            public void onResponse(Call<WalletBalanceResponse> call, Response<WalletBalanceResponse> response) {
                if (response.isSuccessful()) {
                    WalletBalanceResponse body = response.body();

                    if (body.status.contentEquals("Failure")) {
                        callback.onFailure(new IOException(body.errorMessage));
                    }

                    if (body.status.contentEquals("Throttled")) {
                        callback.onFailure(new IOException("Too many requests: Throttled"));
                    }

                    callback.onSuccess(body);
                } else {
                    callback.onFailure(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<WalletBalanceResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

}
