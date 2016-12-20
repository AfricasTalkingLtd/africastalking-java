package com.africastalking;

import okhttp3.*;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.io.IOException;

abstract class Service {

    protected boolean DEBUG = false;
    protected Retrofit.Builder mRetrofitBuilder;


    protected Service(String username, String apiKey, Format format) {
        this(username, apiKey, format, false);
    }

    protected Service(String username, String apiKey) {
        this(username, apiKey, Format.JSON, false);
    }

    protected Service(final String username, final String apiKey, final Format format, boolean debug) {

        Converter.Factory factory;
        switch (format){
            case XML:
                factory = SimpleXmlConverterFactory.create();
                break;
            case JSON:
            default:
                factory = GsonConverterFactory.create();
                break;
        }


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder().addQueryParameter("username", username).build();

                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("apiKey", apiKey)
                        .addHeader("Accept", format.toString())
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        mRetrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(factory)
                .client(httpClient.build());

        initService();
    }

    protected abstract void initService();



}
