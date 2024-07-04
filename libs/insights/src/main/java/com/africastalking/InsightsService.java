package com.africastalking;

import com.africastalking.insights.SimSwapResponse;
import com.google.gson.Gson;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public final class InsightsService extends Service {

    private InsightsService sInstance;
    private IInsights service;



    static class SimSwapRequest {
        public String username;
        public List<String> phoneNumbers;
        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }


    private InsightsService(String username, String apiKey) {
        super(username, apiKey);

    }

    InsightsService() {
        super();
    }

    @Override
    protected InsightsService getInstance(String username, String apiKey) {
        if(sInstance == null) {
            sInstance = new InsightsService(username, apiKey);
        }
        return sInstance;
    }

    @Override
    protected boolean isInitialized() {
        return sInstance != null;
    }

    @Override
    protected void initService() {
        String baseUrl = "https://insights."+ (isSandbox ? Const.SANDBOX_DOMAIN : Const.PRODUCTION_DOMAIN) + "/";
        service = mRetrofitBuilder
                .baseUrl(baseUrl)
                .build()
                .create(IInsights.class);
    }

    @Override
    protected void destroyService() {
        if (sInstance != null) {
            sInstance = null;
        }
    }

    public SimSwapResponse checkSimSwapState(List<String> phoneNumbers) throws IOException {
        Response<SimSwapResponse> resp = service.checkSimSwapState(_makeRequestBody(phoneNumbers)).execute();
        if(!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().toString());
        }
        return resp.body();
    }

    public void checkSimSwapSate(List<String> phoneNumbers, Callback<SimSwapResponse> callback) {
        service.checkSimSwapState(_makeRequestBody(phoneNumbers)).enqueue(makeCallback(callback));
    }


    private String _makeRequestBody(List<String> phoneNumbers) {
        SimSwapRequest req = new SimSwapRequest();
        req.username = mUsername;
        req.phoneNumbers = phoneNumbers;
        return req.toString();
    }
}