package com.africastalking;

import okhttp3.*;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.*;
import retrofit2.Call;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;


/**
 * A given service offered by AT API
 */
abstract class Service {

    Retrofit.Builder mRetrofitBuilder;

    String mUsername;
    Currency mCurrency;

    Service(final String username, final String apiKey, final Format format, Currency currency) {

        mUsername = username;
        mCurrency = currency;


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (AfricasTalking.LOGGING) {
            HttpLoggingInterceptor logger = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    System.err.println(message);
                }
            });
            logger.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logger);
        }

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .addHeader("apiKey", apiKey)
                        .addHeader("Accept", format.toString())
                        .build();

                return chain.proceed(request);
            }
        });

        mRetrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
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
    protected <T> retrofit2.Callback<T> makeCallback(Callback<T> cb) {
        return new retrofit2.Callback<T>() {
            @Override
            public void onResponse(Call<T> call, retrofit2.Response<T> response) {
                cb.onSuccess(response.body());
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
     * @param format
     * @param currency
     * @param <T>
     * @return
     */
    protected abstract <T extends Service> T getInstance(String username, String apiKey, Format format, Currency currency);

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
}
