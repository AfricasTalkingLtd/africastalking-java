package com.africastalking;


import com.africastalking.airtime.AirtimeResponse;

import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.HashMap;


/**
 * Airtime Service; send airtime
 */
public final class AirtimeService extends Service {


    private static AirtimeService sInstance;
    private IAirtime service;
    private int mMaxNumRetry = 0;

    private AirtimeService(String username, String apiKey) {
        super(username, apiKey);
    }

    AirtimeService() {
        super();
    }

    @Override
    protected AirtimeService getInstance(String username, String apiKey) {

        if (sInstance == null) {
            sInstance = new AirtimeService(username, apiKey);
        }

        return sInstance;
    }

    @Override
    protected void initService() {
        String url = "https://api."+ (isSandbox ? Const.SANDBOX_DOMAIN : Const.PRODUCTION_DOMAIN);
        url += "/version1/airtime/";
        Retrofit retrofit = mRetrofitBuilder
                .baseUrl(url)
                .build();

        service = retrofit.create(IAirtime.class);
    }

    @Override
    protected boolean isInitialized() {
        return sInstance != null;
    }

    @Override
    protected void destroyService() {
        if (sInstance != null) {
            sInstance = null;
        }
    }



    public void setIdempotencyKey(String key) {
        this.mIndempotencyKey = key;
    }


    /**
     * Set the maximum number of retries in case of failed airtime deliveries due to telco unavailability or any other reason.
     * @param retires
     */
    public void setMaxRetry(int retires) {
        this.mMaxNumRetry = retires;
    }


    /**
     * Send airtime
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param phone Phone number (international format) to receive the airtime
     * @param currencyCode
     * @param amount Amount to send
     * @return {@link com.africastalking.airtime.AirtimeResponse AirtimeResponse}
     * @throws IOException
     */
    public AirtimeResponse send(String phone, String currencyCode, float amount) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        map.put(phone, currencyCode + " " + amount);
        return send(map);
    }

    /**
     * Send airtime
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param phone Phone number (international format) to receive the airtime
     * @param currencyCode
     * @param amount Amount to send
     * @param callback {@link com.africastalking.Callback Callback} to be called once a response is received
     */
    public void send(String phone, String currencyCode, float amount, Callback<AirtimeResponse> callback) {
        HashMap<String, String> map = new HashMap<>();
        map.put(phone, currencyCode + " " + amount);
        send(map, callback);
    }

    /**
     * Send airtime
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param recipients Phone numbers with amounts to send e.g. UGX 854
     * @return {@link com.africastalking.airtime.AirtimeResponse AirtimeResponse}
     * @throws IOException
     */
    public AirtimeResponse send(HashMap<String, String> recipients) throws IOException {
        String json = _makeRecipientsJSON(recipients);
        Response<AirtimeResponse> resp = service.send(mUsername, json, mMaxNumRetry > 0 ? String.valueOf(mMaxNumRetry) : null).execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    /**
     * Send airtime
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param recipients Phone numbers with amounts to send e.g. UGX 854
     * @param callback {@link com.africastalking.Callback Callback} to be called once a response is received
     */
    public void send(HashMap<String, String> recipients, Callback<AirtimeResponse> callback) {
        try{
            String json = _makeRecipientsJSON(recipients);
            service.send(mUsername, json, mMaxNumRetry > 0 ? String.valueOf(mMaxNumRetry) : null).enqueue(makeCallback(callback));
        }catch (IOException ioe) {
            callback.onFailure(ioe);
        }
    }


    private String _makeRecipientsJSON(HashMap<String, String> recipients) throws IOException {

        if (recipients == null || recipients.size() == 0) {
            throw new IOException("Invalid recipients");
        }

        StringBuilder body = new StringBuilder();
        int count = recipients.size();
        for (String phone:recipients.keySet()) {
            checkPhoneNumber(phone);
            String amount = recipients.get(phone);
            String target = "{\"phoneNumber\":\"" + phone + "\", \"amount\": \""+ amount +"\"}";
            body.append(target);

            if (count > 1) {
                body.append(",");
            }
            count--;
        }

        return "[" + body.toString() + "]";
    }

}
