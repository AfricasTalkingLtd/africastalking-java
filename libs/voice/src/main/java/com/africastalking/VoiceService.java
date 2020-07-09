package com.africastalking;

import com.africastalking.voice.CallResponse;
import com.africastalking.voice.CallTransferResponse;
import com.africastalking.voice.QueuedCallsResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.io.IOException;

public final class VoiceService extends Service {

    private VoiceService sInstance;
    private IVoice voice;

    private VoiceService(String username, String apiKey) {
        super(username, apiKey);
    }


    VoiceService() {
        super();
    }

    @Override
    protected VoiceService getInstance(String username, String apiKey) {
        if (sInstance == null) {
            sInstance = new VoiceService(username, apiKey);
        }
        return sInstance;
    }

    @Override
    protected boolean isInitialized() {
        return sInstance != null;
    }

    @Override
    protected void initService() {
        String baseUrl = "https://voice."+ (isSandbox ? Const.SANDBOX_DOMAIN : Const.PRODUCTION_DOMAIN) + "/";
        voice = mRetrofitBuilder
                .baseUrl(baseUrl)
                .build()
                .create(IVoice.class);
    }

    @Override
    protected void destroyService() {
        if (sInstance != null) {
            sInstance = null;
        }
    }

    /**
     * Initiate phone call
     * @param to Phone number to call
     * @param from Number from which to initiate the call
     * @return {@link com.africastalking.voice.CallResponse CallResponse}
     * @throws IOException
     */
    public CallResponse call(String to, String from) throws IOException {
        checkPhoneNumber(to);
        Call<CallResponse> call = voice.call(mUsername, to, from);
        Response<CallResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    public CallResponse call(String to) throws IOException {
        return call(to, "");
    }


    /**
     * Initiate phone call
     * @param to Phone number to call
     * @param from Number from which to initiate the call
     * @param callback {@link com.africastalking.Callback Callback}
     */
    public void call(String to, String from, final Callback<CallResponse> callback) {
        try {
            checkPhoneNumber(to);
            Call<CallResponse> call = voice.call(mUsername, to, from);
            call.enqueue(makeCallback(callback));
        } catch (IOException ex){
            callback.onFailure(ex);
        }
    }

    public void call(String to, Callback<CallResponse> callback) {
        call(to, "", callback);
    }


    /**
     * Initiate a call transfer
     * @param phoneNumber Phone Number to transfer the call to
     * @param sessionId Session Id of the ongoing call, it must have two legs
     * @return
     * @throws IOException
     */
    public CallTransferResponse callTransfer(String phoneNumber, String sessionId) throws IOException {
        return callTransfer(phoneNumber, sessionId, "", "");
    }
    public void callTransfer(String phoneNumber, String sessionId, Callback<CallTransferResponse> callback) {
        callTransfer(phoneNumber, sessionId,"", "", callback);
    }

    public CallTransferResponse callTransfer(String phoneNumber, String sessionId, String callLeg) throws IOException {
        return callTransfer(phoneNumber, sessionId, callLeg, "");
    }
    public void callTransfer(String phoneNumber, String sessionId, String callLeg, Callback<CallTransferResponse> callback) {
       callTransfer(phoneNumber, sessionId, callLeg, "", callback);
    }

    public CallTransferResponse callTransfer(String phoneNumber, String sessionId, String callLeg, String holdMusicUrl) throws IOException {
        checkPhoneNumber(phoneNumber);
        Call<CallTransferResponse> call = voice.callTransfer(mUsername, phoneNumber, sessionId, callLeg.isEmpty() ? null : callLeg, holdMusicUrl.isEmpty() ? null : holdMusicUrl);
        Response<CallTransferResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }
    public void callTransfer(String phoneNumber, String sessionId, String callLeg, String holdMusicUrl, Callback<CallTransferResponse> callback) {
        try {
            checkPhoneNumber(phoneNumber);
            Call<CallTransferResponse> call = voice.callTransfer(mUsername, phoneNumber, sessionId, callLeg.isEmpty() ? null : callLeg, holdMusicUrl.isEmpty() ? null : holdMusicUrl);
            call.enqueue(makeCallback(callback));
        } catch (IOException ex){
            callback.onFailure(ex);
        }
    }


    /**
     * Fetch queued calls
     * @param phoneNumber Your virtual phone number
     * @return {@link com.africastalking.voice.QueuedCallsResponse QueuedCallsResponse}
     * @throws IOException
     */
    public QueuedCallsResponse fetchQueuedCalls(String phoneNumber) throws IOException {
        Call<QueuedCallsResponse> call = voice.queueStatus(mUsername, phoneNumber);
        Response<QueuedCallsResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    public void fetchQueuedCalls(String phoneNumber, final Callback<QueuedCallsResponse> callback) {
        Call<QueuedCallsResponse> call = voice.queueStatus(mUsername, phoneNumber);
        call.enqueue(makeCallback(callback));
    }

    /**
     * Upload media file
     * @param phoneNumber Your virtual phone number
     * @param url URL to your media file
     * @throws IOException
     */
    public String uploadMediaFile(String phoneNumber, String url) throws IOException {
        Call<String> call = voice.mediaUpload(mUsername, url, phoneNumber);
        Response<String> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    public void uploadMediaFile(String phoneNumber, String url, final Callback<String> callback) {
        Call<String> call = voice.mediaUpload(mUsername, url, phoneNumber);
        call.enqueue(makeCallback(callback));
    }

}
