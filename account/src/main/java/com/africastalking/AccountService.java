package com.africastalking;


import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

/**
 * AT Account service. Retrieve user account info
 */
public final class AccountService extends Service {

    private static AccountService sInstance;
    private IAccount service;

    private AccountService(String username, String apiKey, Format format, boolean debug) {
        super(username, apiKey, format, debug);
    }

    AccountService() {
        super();
    }

    @Override
    protected AccountService getInstance(String username, String apiKey, Format format, boolean debug) {

        if (sInstance == null) {
            sInstance = new AccountService(username, apiKey, format, debug);
        }

        return sInstance;
    }

    @Override
    protected void initService() {
        String url = "https://api."+ (DEBUG ? Const.SANDBOX_DOMAIN : Const.BASE_DOMAIN) + "/version1/";
        Retrofit retrofit = mRetrofitBuilder
                .baseUrl(url)
                .build();

        service = retrofit.create(IAccount.class);
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
     * Get user info.
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @return String in specified format, xml or json
     * @throws IOException
     */
    public String fetchUser() throws IOException {
        Response<String> resp = service.fetchUser().execute();
        return resp.body().trim();
    }

    /**
     * Get user info.
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param callback
     */
    public void fetchUser(final Callback<String> callback) {
        service.fetchUser().enqueue(makeCallback(callback));
    }



}
