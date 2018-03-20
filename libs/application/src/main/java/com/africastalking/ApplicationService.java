package com.africastalking;


import com.africastalking.application.ApplicationResponse;

import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

/**
 * Account service. Retrieve app info
 */
public class ApplicationService extends Service {

    private static ApplicationService sInstance;
    private IApplication service;

    private ApplicationService(String username, String apiKey) {
        super(username, apiKey);
    }

    ApplicationService() {
        super();
    }

    @Override
    protected ApplicationService getInstance(String username, String apiKey) {

        if (sInstance == null) {
            sInstance = new ApplicationService(username, apiKey);
        }

        return sInstance;
    }

    @Override
    protected void initService() {
        String url = "https://api."+ (isSandbox ? Const.SANDBOX_DOMAIN : Const.PRODUCTION_DOMAIN);
        url += "/version1/";
        Retrofit retrofit = mRetrofitBuilder
                .baseUrl(url)
                .build();

        service = retrofit.create(IApplication.class);
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


    // ->

    /**
     * Get app info.
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @return String in specified format, xml or json
     * @throws IOException
     */
    public ApplicationResponse fetchApplicationData() throws IOException {
        Response<ApplicationResponse> resp = service.fetchApplicationData(mUsername).execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }

    /**
     * Get app info.
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param callback {@link com.africastalking.Callback Callback}
     */
    public void fetchApplicationData(final Callback<ApplicationResponse> callback) {
        service.fetchApplicationData(mUsername).enqueue(makeCallback(callback));
    }
}
