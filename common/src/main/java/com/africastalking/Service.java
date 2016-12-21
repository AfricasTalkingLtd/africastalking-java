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

    boolean DEBUG = false;
    Retrofit.Builder mRetrofitBuilder;

    Service(final String username, final String apiKey, final Format format, boolean debug) {

        DEBUG = debug;


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (DEBUG) {
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
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder().addQueryParameter("username", username).build();

                Request request = original.newBuilder()
                        .addHeader("apiKey", apiKey)
                        .addHeader("Accept", format.toString())
                        .url(url)
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
     * Get am instance of a service. It
     * @param username
     * @param apiKey
     * @param format
     * @param debug
     * @param <T>
     * @return
     */
    protected abstract <T extends Service> T getInstance(String username, String apiKey, Format format, boolean debug);

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
