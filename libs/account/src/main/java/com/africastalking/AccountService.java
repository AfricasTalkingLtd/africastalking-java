package com.africastalking;


import com.africastalking.account.AccountResponse;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * Account service. Retrieve user account info
 */
public class AccountService extends Service {

    private static AccountService sInstance;
    private IAccount service;

    private AccountService(String username, String apiKey) {
        super(username, apiKey);
    }

    AccountService() {
        super();
    }

    @Override
    protected AccountService getInstance(String username, String apiKey) {

        if (sInstance == null) {
            sInstance = new AccountService(username, apiKey);
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
    public AccountResponse getUser() throws IOException {
        Response<AccountResponse> resp = service.getUser(mUsername).execute();
        return resp.body();
    }

    /**
     * Get user info.
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     * occurred
     * </p>
     * @param callback {@link com.africastalking.Callback Callback}
     */
    public void getUser(final Callback<AccountResponse> callback) {
        service.getUser(mUsername).enqueue(makeCallback(callback));
    }
}
