package com.africastalking;

import com.africastalking.voice.CallResponse;
import com.africastalking.voice.QueuedCallsResponse;

import retrofit2.Call;
import retrofit2.Response;

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
        Call<CallResponse> call = voice.call(mUsername, to, from);
        Response<CallResponse> resp = call.execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.message());
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
        Call<CallResponse> call = voice.call(mUsername, to, from);
        call.enqueue(makeCallback(callback));
    }

    public void call(String to, Callback<CallResponse> callback) {
        call(to, "", callback);
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
            throw new IOException(resp.message());
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
            return resp.message();
        }
        return resp.body();
    }

    public void uploadMediaFile(String phoneNumber, String url, final Callback<String> callback) {
        Call<String> call = voice.mediaUpload(mUsername, url, phoneNumber);
        call.enqueue(makeCallback(callback));
    }

}
