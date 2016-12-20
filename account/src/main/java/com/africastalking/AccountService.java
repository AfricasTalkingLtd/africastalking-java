package com.africastalking;


import retrofit2.Call;
import retrofit2.Retrofit;

public class AccountService extends Service implements IAccount {

    private IAccount service;

    protected AccountService(String username, String apiKey, Format format) {
        super(username, apiKey, format);
    }

    protected AccountService(String username, String apiKey) {
        super(username, apiKey);
    }

    protected AccountService(String username, String apiKey, Format format, boolean debug) {
        super(username, apiKey, format, debug);
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
