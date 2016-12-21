package com.africastalking;


import retrofit2.Call;
import retrofit2.Retrofit;

public class AccountService extends Service implements IAccount {

    private static AccountService sInstance;
    private IAccount service;

    AccountService(String username, String apiKey, Format format, boolean debug) {
        super(username, apiKey, format, debug);
    }

    AccountService() {
        super();
    }

    @Override
    protected Service getInstance(String username, String apiKey, Format format, boolean debug) {

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
    public Call<String> fetchUser() {
        return service.fetchUser();
    }


}
