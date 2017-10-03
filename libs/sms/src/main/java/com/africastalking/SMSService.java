package com.africastalking;


import retrofit2.Response;

import java.io.IOException;
import java.util.StringJoiner;


/**
 * SMS Service; Send and fetch SMSs
 */
public final class SMSService extends Service {


    private static SMSService sInstance;
    private ISMS sms;

    private SMSService(String username, String apiKey) {
        super(username, apiKey);
    }

    SMSService() {
        super();
    }

    @Override
    protected SMSService getInstance(String username, String apiKey) {

        if (sInstance == null) {
            sInstance = new SMSService(username, apiKey);
        }

        return sInstance;
    }

    @Override
    protected void initService() {
        String baseUrl = "https://api."+ (isSandbox ? Const.SANDBOX_DOMAIN : Const.PRODUCTION_DOMAIN) + "/version1/";
        sms = mRetrofitBuilder.baseUrl(baseUrl).build().create(ISMS.class);
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

    private String formatRecipients(String[] recipients) {

        if (recipients == null){
            return null;
        }

        if (recipients.length == 1) {
            return recipients[0];
        }

        StringJoiner joiner = new StringJoiner(",");
        for (CharSequence cs: recipients) {
            joiner.add(cs);
        }
        return joiner.toString();
    }

    // -> Normal

    /**
     * Send a message
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param message
     * @param from
     * @param recipients
     * @return
     * @throws IOException
     */
    public String send(String message, String from, String[] recipients) throws IOException {
        Response<String> resp = sms.send(mUsername, formatRecipients(recipients), from, message).execute();
        if (!resp.isSuccessful()) {
            return resp.message();
        }
        return resp.body();
    }

    /**
     * Send a message
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param message
     * @param from
     * @param recipients
     * @param callback
     */
    public void send(String message, String from, String[] recipients, Callback<String> callback) {
        sms.send(mUsername, formatRecipients(recipients), from, message).enqueue(makeCallback(callback));
    }

    /**
     * Send a message
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param message
     * @param recipients
     * @return
     * @throws IOException
     */
    public String send(String message, String[] recipients) throws IOException {
        return send(message, null, recipients);
    }


    /**
     * Send a message
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param message
     * @param recipients
     * @param callback
     */
    public void send(String message, String[] recipients, Callback<String> callback) {
        send(message, null, recipients, callback);
    }


    // -> Bulk

    /**
     * Send a message in bulk
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param message
     * @param from
     * @param enqueue
     * @param recipients
     * @return
     * @throws IOException
     */
    public String sendBulk(String message, String from, boolean enqueue, String[] recipients) throws IOException {
        Response<String> resp = sms.sendBulk(mUsername,
                formatRecipients(recipients), from, message,
                1, enqueue ? "1":null).execute();

        if (!resp.isSuccessful()) {
            return resp.message();
        }
        return resp.body();
    }

    /**
     * Send a message in bulk
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param message
     * @param from
     * @param enqueue
     * @param recipients
     * @param callback
     */
    public void sendBulk(String message, String from, boolean enqueue, String[] recipients, Callback<String> callback) {
        sms.sendBulk(mUsername,
                formatRecipients(recipients), from, message,
                1, enqueue ? "1":null).enqueue(makeCallback(callback));
    }

    /**
     * Send a message in bulk
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param message
     * @param from
     * @param recipients
     * @return
     * @throws IOException
     */
    public String sendBulk(String message, String from, String[] recipients) throws IOException {
        return sendBulk(message, from, false, recipients);
    }

    /**
     * Send a message in bulk
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param message
     * @param from
     * @param recipients
     * @param callback
     */
    public void sendBulk(String message, String from, String[] recipients, Callback<String> callback) {
        sendBulk(message, from, false, recipients, callback);
    }

    /**
     * Send a message in bulk
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param message
     * @param enqueue
     * @param recipients
     * @return
     * @throws IOException
     */
    public String sendBulk(String message, boolean enqueue, String[] recipients) throws IOException {
        return sendBulk(message, null, enqueue, recipients);
    }

    /**
     * Send a message in bulk
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param message
     * @param enqueue
     * @param recipients
     * @param callback
     */
    public void sendBulk(String message, boolean enqueue, String[] recipients, Callback<String> callback) {
        sendBulk(message, null, enqueue, recipients, callback);
    }

    /**
     * Send a message in bulk
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param message
     * @param recipients
     * @return
     * @throws IOException
     */
    public String sendBulk(String message, String[] recipients) throws IOException {
        return sendBulk(message, null, false, recipients);
    }

    /**
     * Send a message in bulk
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param message
     * @param recipients
     * @param callback
     */
    public void sendBulk(String message, String[] recipients, Callback<String> callback) {
        sendBulk(message, null, false, recipients, callback);
    }


    // -> Premium

    /**
     * Send premium SMS
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param message
     * @param from
     * @param keyword
     * @param linkId
     * @param retryDurationInHours
     * @param recipients
     * @return
     * @throws IOException
     */
    public String sendPremium(String message, String from, String keyword, String linkId, long retryDurationInHours, String[] recipients) throws IOException {
        String retryDuration = retryDurationInHours <= 0 ? null : String.valueOf(retryDurationInHours);
        Response<String> resp = sms.sendPremium(mUsername, formatRecipients(recipients), from, message, keyword, linkId, retryDuration).execute();
        if (!resp.isSuccessful()) {
            return resp.message();
        }
        return resp.body();
    }

    /**
     * Send premium SMS
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param message
     * @param from
     * @param keyword
     * @param linkId
     * @param retryDurationInHours
     * @param recipients
     * @param callback
     */
    public void sendPremium(String message, String from, String keyword, String linkId, long retryDurationInHours, String[] recipients, Callback<String> callback) {
        String retryDuration = retryDurationInHours <= 0 ? null : String.valueOf(retryDurationInHours);
        sms.sendPremium(mUsername, formatRecipients(recipients),
                from, message, keyword, linkId, retryDuration)
                .enqueue(makeCallback(callback));
    }

    /**
     * Send premium SMS
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param message
     * @param keyword
     * @param linkId
     * @param retryDurationInHours
     * @param recipients
     * @return
     * @throws IOException
     */
    public String sendPremium(String message, String keyword, String linkId, long retryDurationInHours, String[] recipients) throws IOException {
        return sendPremium(message, null, keyword, linkId, retryDurationInHours, recipients);
    }

    /**
     * Send premium SMS
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param message
     * @param keyword
     * @param linkId
     * @param retryDurationInHours
     * @param recipients
     * @param callback
     */
    public void sendPremium(String message, String keyword, String linkId, long retryDurationInHours, String[] recipients, Callback<String> callback){
        sendPremium(message, null, keyword, linkId, retryDurationInHours, recipients, callback);
    }

    /**
     * Send Premium SMS
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param message
     * @param from
     * @param keyword
     * @param linkId
     * @param recipients
     * @return
     * @throws IOException
     */
    public String sendPremium(String message, String from, String keyword, String linkId, String[] recipients) throws IOException {
        return sendPremium(message, from, keyword, linkId, -1, recipients);
    }

    /**
     * Send premium SMS
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param message
     * @param from
     * @param keyword
     * @param linkId
     * @param recipients
     * @param callback
     */
    public void sendPremium(String message, String from, String keyword, String linkId, String[] recipients, Callback<String> callback){
        sendPremium(message, from, keyword, linkId, -1, recipients, callback);
    }

    /**
     * Send premium SMS
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param message
     * @param keyword
     * @param linkId
     * @param recipients
     * @return
     * @throws IOException
     */
    public String sendPremium(String message, String keyword, String linkId, String[] recipients) throws IOException {
        return sendPremium(message, null, keyword, linkId, -1, recipients);
    }

    /**
     * Send premium SMS
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param message
     * @param keyword
     * @param linkId
     * @param recipients
     * @param callback
     */
    public void sendPremium(String message, String keyword, String linkId, String[] recipients, Callback<String> callback){
        sendPremium(message, null, keyword, linkId, -1, recipients, callback);
    }

    // -> Fetch Message

    /**
     * Fetch messages
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param lastReceivedId
     * @return
     * @throws IOException
     */
    public String fetchMessage(String lastReceivedId) throws IOException {
        Response<String> resp = sms.fetchMessage(mUsername, lastReceivedId).execute();
        if (!resp.isSuccessful()) {
            return resp.message();
        }
        return resp.body();
    }

    /**
     * Fetch messages
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @return
     * @throws IOException
     */
    public String fetchMessage() throws IOException {
        return fetchMessage("0");
    }

    /**
     * Fetch messages
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param lastReceivedId
     * @param callback
     */
    public void fetchMessage(String lastReceivedId, Callback<String> callback) {
        sms.fetchMessage(mUsername, lastReceivedId).enqueue(makeCallback(callback));
    }

    /**
     * Fetch messages
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param callback
     */
    public void fetchMessage(Callback<String> callback) {
        fetchMessage("0", callback);
    }

    // -> Fetch Subscription

    /**
     * Fetch subscriptions
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param shortCode
     * @param keyword
     * @param lastReceivedId
     * @return
     * @throws IOException
     */
    public String fetchSubscription(String shortCode, String keyword, String lastReceivedId) throws IOException {
        Response<String> resp = sms.fetchSubsciption(mUsername, shortCode, keyword, lastReceivedId).execute();
        if (!resp.isSuccessful()) {
            return resp.message();
        }
        return resp.body();
    }

    /**
     * Fetch subscription
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param shortCode
     * @param keyword
     * @param lastReceivedId
     * @param callback
     */
    public void fetchSubscription(String shortCode, String keyword, String lastReceivedId, Callback<String> callback) {
        sms.fetchSubsciption(mUsername, shortCode, keyword, lastReceivedId).enqueue(makeCallback(callback));

    }

    /**
     * Fetch subscriptions
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param shortCode
     * @param keyword
     * @return
     * @throws IOException
     */
    public String fetchSubscription(String shortCode, String keyword) throws IOException {
        return fetchSubscription(shortCode, keyword, "0");
    }


    /**
     * Create subscription
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param shortCode
     * @param keyword
     * @param callback
     */
    public void fetchSubscription(String shortCode, String keyword, Callback<String> callback) {
        fetchSubscription(shortCode, keyword, "0", callback);
    }

    // -> Create subscription

    /**
     * Create subscription
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param shortCode
     * @param keyword
     * @param phoneNumber
     * @param checkoutToken
     * @return
     * @throws IOException
     */
    public String createSubscription(String shortCode, String keyword, String phoneNumber, String checkoutToken) throws IOException {
        Response<String> resp = sms.createSubscription(mUsername, shortCode, keyword, phoneNumber, checkoutToken).execute();
        if (!resp.isSuccessful()) {
            return resp.message();
        }
        return resp.body();
    }

    /**
     * Create subscription
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param shortCode
     * @param keyword
     * @param phoneNumber
     * @param checkoutToken
     * @param callback
     */
    public void createSubscription(String shortCode, String keyword, String phoneNumber, String checkoutToken, Callback<String> callback) {
        sms.createSubscription(mUsername, shortCode, keyword, phoneNumber, checkoutToken).enqueue(makeCallback(callback));
    }


}
