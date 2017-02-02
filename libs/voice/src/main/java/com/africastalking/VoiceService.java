package com.africastalking;

import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public final class VoiceService extends Service {

    private VoiceService sInstance;
    private IVoice voice;

    private VoiceService(String username, String apiKey, Format format) {
        super(username, apiKey, format, Currency.KES);
    }


    VoiceService() {
        super();
    }

    @Override
    protected VoiceService getInstance(String username, String apiKey, Format format, Currency currency) {
        if (sInstance == null) {
            sInstance = new VoiceService(username, apiKey, format);
        }
        return sInstance;
    }

    @Override
    protected boolean isInitialized() {
        return sInstance != null;
    }

    @Override
    protected void initService() {
        String baseUrl = "https://voice."+ (AfricasTalking.ENV == Environment.SANDBOX ? Const.SANDBOX_DOMAIN : Const.PRODUCTION_DOMAIN) + "/";
        voice = mRetrofitBuilder.baseUrl(baseUrl).build().create(IVoice.class);
    }

    @Override
    protected void destroyService() {
        if (sInstance != null) {
            sInstance = null;
        }
    }

    /**
     * Initiate phone call
     * @param to
     * @param from
     * @return
     * @throws IOException
     */
    public boolean call(String to, String from) throws IOException {
        Call<String> call = voice.call(mUsername, to, from);
        Response<String> resp = call.execute();
        return resp.code() == 201;
    }

    /**
     *
     * @param to
     * @return
     * @throws IOException
     */
    public boolean call(String to) throws IOException {
        return call(to, "");
    }


    /**
     *
     * @param to
     * @param from
     * @param callback
     */
    public void call(String to, String from, final Callback<Boolean> callback) {
        Call<String> call = voice.call(mUsername, to, from);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                boolean success = response.code() == 201;
                if (success) {
                    callback.onSuccess(true);
                }else {
                    callback.onFailure(new Exception(response.body()));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    /**
     *
     * @param to
     * @param callback
     */
    public void call(String to, Callback<Boolean> callback) {
        call(to, "", callback);
    }


    /**
     *
     * @param phoneNumber
     * @return
     * @throws IOException
     */
    public String fetchQueuedCalls(String phoneNumber) throws IOException {
        Call<String> call = voice.queueStatus(mUsername, phoneNumber);
        Response<String> resp = call.execute();
        return resp.body();
    }

    /**
     *
     * @param phoneNumber
     * @param callback
     */
    public void fetchQueuedCalls(String phoneNumber, final Callback<String> callback) {
        Call<String> call = voice.queueStatus(mUsername, phoneNumber);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                boolean success = response.code() == 201;
                if (success) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception(response.body()));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


}
