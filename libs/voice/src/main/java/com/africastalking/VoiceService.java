package com.africastalking;

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
    public String call(String to, String from) throws IOException {
        Call<String> call = voice.call(mUsername, to, from);
        Response<String> resp = call.execute();
        return resp.body();
    }

    /**
     *
     * @param to
     * @return
     * @throws IOException
     */
    public String call(String to) throws IOException {
        return call(to, "");
    }


    /**
     *
     * @param to
     * @param from
     * @param callback
     */
    public void call(String to, String from, final Callback<String> callback) {
        Call<String> call = voice.call(mUsername, to, from);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                boolean success = response.code() == 201;
                if (success) {
                    callback.onSuccess(response.body());
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
    public void call(String to, Callback<String> callback) {
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
        if (!resp.isSuccessful()) {
            return resp.message();
        }
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
                    String body = response.body();
                    callback.onFailure(new Exception(body != null ? body : response.message()));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


}
