package com.africastalking;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;

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

    protected abstract Service getInstance(String username, String apiKey, Format format, boolean debug);

    protected abstract void initService();
}
