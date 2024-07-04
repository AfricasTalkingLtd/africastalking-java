package com.africastalking;

import com.africastalking.mobileData.recipient.MobileDataRecipient;
import com.africastalking.mobileData.response.*;
import com.africastalking.mobileData.Transaction;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public final class MobileDataService extends Service {

    private MobileDataService sInstance;
    private IMobileData mobileData;


    private MobileDataService(String username, String apiKey) {
        super(username, apiKey);
    }

    MobileDataService() {
        super();
    }

    @Override
    protected MobileDataService getInstance(String username, String apiKey) {
        if(sInstance == null) {
            sInstance = new MobileDataService(username, apiKey);
        }
        return sInstance;
    }

    @Override
    protected boolean isInitialized() {
        return sInstance != null;
    }

    @Override
    protected void initService() {
        String baseUrl = "https://bundles."+ (isSandbox ? Const.SANDBOX_DOMAIN : Const.PRODUCTION_DOMAIN) + "/";
        mobileData = mRetrofitBuilder
                .baseUrl(baseUrl)
                .build()
                .create(IMobileData.class);
    }

    @Override
    protected void destroyService() {
        if (sInstance != null) {
            sInstance = null;
        }
    }

    public void setIdempotencyKey(String key) {
        this.mIdempotencyKey = key;
    }


    private HashMap<String, Object> makeMobileDataRequest(String product, List<MobileDataRecipient> recipients) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", mUsername);
        body.put("productName", product);
        body.put("recipients", recipients);
        return body;
    }

    /**
     * Send mobile data from product to recipients
     * @param product
     * @param recipients
     * @return
     * @throws IOException
     */
    public MobileDataResponse send(String product, List<MobileDataRecipient> recipients) throws IOException {
        for(MobileDataRecipient recipient : recipients) { checkPhoneNumber(recipient.phoneNumber); }

        HashMap<String, Object> body = makeMobileDataRequest(product, recipients);
        Call<MobileDataResponse> call = mobileData.requestMobileData(body);
        Response<MobileDataResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    /**
     * Send mobile data from product to recipients
     * @param product
     * @param recipients
     * @param callback
     */
    public void send(String product, List<MobileDataRecipient> recipients, Callback<MobileDataResponse> callback) {
        try {
            for(MobileDataRecipient recipient : recipients) { checkPhoneNumber(recipient.phoneNumber); }

            HashMap<String, Object> body = makeMobileDataRequest(product, recipients);
            Call<MobileDataResponse> call = mobileData.requestMobileData(body);
            call.enqueue(makeCallback(callback));

        } catch (IOException ex) {
            callback.onFailure(ex);
        }
    }

    /**
     * Find a transaction by id
     * @param transactionId
     * @return
     * @throws IOException
     */
    public Transaction findTransaction(String transactionId) throws IOException {
        HashMap<String, String> query = new HashMap<String, String>();
        query.put("username", mUsername);
        query.put("transactionId", transactionId);
        Call<FindTransactionResponse> call = mobileData.findTransaction(query);
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

    /**
     * Find a transaction by id
     * @param transactionId
     * @param callback
     */
    public void findTransaction(String transactionId, Callback<Transaction> callback) {
        HashMap<String, String> query = new HashMap<String, String>();
        query.put("username", mUsername);
        query.put("transactionId", transactionId);
        Call<FindTransactionResponse> call = mobileData.findTransaction(query);
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

    /**
     * Fetch wallet balance
     * @return
     * @throws IOException
     */
    public WalletBalanceResponse fetchWalletBalance() throws IOException {
        HashMap<String, String> filters = new HashMap<>();
        filters.put("username", mUsername);

        Call<WalletBalanceResponse> call = mobileData.fetchWalletBalance(filters);
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

    /**
     * Fetch wallet balance
     * @param callback
     */
    public void fetchWalletBalance(Callback<WalletBalanceResponse> callback) {
        HashMap<String, String> filters = new HashMap<>();
        filters.put("username", mUsername);

        Call<WalletBalanceResponse> call = mobileData.fetchWalletBalance(filters);
        
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
