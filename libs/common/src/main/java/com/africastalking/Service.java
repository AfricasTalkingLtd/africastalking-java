package com.africastalking;

import okhttp3.*;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.*;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;


/**
 * A given service offered by AT API
 */
abstract class Service {

    Retrofit.Builder mRetrofitBuilder;
    String mUsername;

    protected String mIndempotencyKey = null;

    static boolean isSandbox = false;
    static Logger LOGGER = null;

    Service(final String username, final String apiKey) {

        mUsername = username;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl url = original.url();
            if (AfricasTalking.hostOverride != null) {
                url = url.newBuilder()
                    .host(AfricasTalking.hostOverride)
                    .build();
            }
            Request.Builder builder = original.newBuilder()
                    .url(url)
                    .addHeader("apiKey", apiKey)
                    .addHeader("Accept", "application/json");

            if (mIndempotencyKey != null) {
                builder.addHeader("Idempotency-Key", mIndempotencyKey);
                mIndempotencyKey = null;
            }

            return chain.proceed(builder.build());
        });

        if (LOGGER != null) {
            HttpLoggingInterceptor logger = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    LOGGER.log(message);
                }
            });
            logger.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logger);
        }

        mRetrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        initService();
    }

    Service() {}


    /**
     *
     * @param cb
     * @param <T>
     * @return
     */
    protected <T> retrofit2.Callback<T> makeCallback(final Callback<T> cb) {
        return new retrofit2.Callback<T>() {
            @Override
            public void onResponse(Call<T> call, retrofit2.Response<T> response) {
                if (response.isSuccessful()) {
                    cb.onSuccess(response.body());
                } else {
                    cb.onFailure(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                cb.onFailure(t);
            }
        };
    }

    /**
     * Get an instance of a service.
     * @param username
     * @param apiKey
     * @param <T>
     * @return
     */
    protected abstract <T extends Service> T getInstance(String username, String apiKey);

    /**
     * Check if a service is initialized
     * @return boolean true if yes, false otherwise
     */
    protected abstract boolean isInitialized();

    /**
     * Initializes a service
     */
    protected abstract void initService();

    /**
     * Destroys a service
     */
    protected abstract void destroyService();

    protected boolean checkPhoneNumber(String phone) throws IOException {
        if (!phone.matches(Const.INTL_PHONE_FORMAT)) {
            throw new IOException("Invalid phone number: " + phone + "; Expecting number in format +XXXxxxxxxxxx");
        }
        return true;
    }
}
