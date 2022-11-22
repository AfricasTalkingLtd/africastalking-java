package com.africastalking;


import com.africastalking.chat.*;
import retrofit2.Response;

import java.io.IOException;

/**
 * Chat Service;
 */
public final class ChatService extends Service {

    private static ChatService sInstance;
    private IChat chat;

    private ChatService(String username, String apiKey) {
        super(username, apiKey);
    }

    ChatService() {
        super();
    }

    @Override
    protected ChatService getInstance(String username, String apiKey) {

        if (sInstance == null) {
            sInstance = new ChatService(username, apiKey);
        }

        return sInstance;
    }

    @Override
    protected void initService() {
        String baseUrl = "https://chat."+ (isSandbox ? Const.SANDBOX_DOMAIN : Const.PRODUCTION_DOMAIN);
        chat = mRetrofitBuilder.baseUrl(baseUrl).build().create(IChat.class);
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


    /* -> Message & Template */


    /**
     * Send a message
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param productId
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @param body
     * @return
     * @throws IOException
     */
    public ChatResponse sendMessage(String productId, String customerNumber, Channel channel, String channelNumber, MessageBody body) throws IOException {
        if (channel == Channel.WhatApp) {
            checkPhoneNumber(customerNumber);
        }
        ChatMessage message = new ChatMessage();
        message.body = body;
        message.username = mUsername;
        message.productId = productId;
        message.customerNumber = customerNumber;
        message.channel = channel.name();
        message.channelNumber = channelNumber;
        Response<ChatResponse> resp = chat.sendMessage(message).execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }


    /**
     * Send a message with text
     * @param productId
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @param text
     * @return
     * @throws IOException
     */
    public ChatResponse sendMessage(String productId, String customerNumber, Channel channel, String channelNumber, String text) throws IOException {
        return sendMessage(productId, customerNumber, channel, channelNumber, new TextMessageBody(text));
    }

    /**
     * Send a message with media
     * @param productId
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @param mediaType
     * @param url
     * @return
     * @throws IOException
     */
    public ChatResponse sendMessage(String productId, String customerNumber, Channel channel, String channelNumber, MediaType mediaType, String url) throws IOException {
        return sendMessage(productId, customerNumber, channel, channelNumber, new MediaMessageBody(mediaType.name(), url));
    }

    /**
     * Send a message with location
     * @param productId
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @param latitude
     * @param longitude
     * @return
     * @throws IOException
     */
    public ChatResponse sendMessage(String productId, String customerNumber, Channel channel, String channelNumber, float latitude, float longitude) throws IOException {
        return sendMessage(productId, customerNumber, channel, channelNumber, new LocationMessageBody(latitude, longitude));
    }


    /**
     * Send a template
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param productId
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @param template
     * @return
     * @throws IOException
     */
    public ChatResponse sendTemplate(String productId, String customerNumber, Channel channel, String channelNumber, Template template) throws IOException {
        return sendMessage(productId, customerNumber, channel, channelNumber, new TextMessageTemplate(template));
    }


    /**
     * Send a message
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     *     occurred
     * </p>
     * @param productId
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @param body
     * @param callback
     */
    public void sendMessage(String productId, String customerNumber, Channel channel, String channelNumber, MessageBody body, Callback<ChatResponse> callback) {
        try {
            if (channel == Channel.WhatApp) {
                checkPhoneNumber(customerNumber);
            }

            ChatMessage message = new ChatMessage();
            message.body = body;
            message.username = mUsername;
            message.productId = productId;
            message.customerNumber = customerNumber;
            message.channel = channel.name();
            message.channelNumber = channelNumber;

            chat.sendMessage(message).enqueue(makeCallback(new Callback<ChatResponse>() {
                @Override
                public void onSuccess(ChatResponse data) {
                    callback.onSuccess(data);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            }));

        } catch (IOException ex) {
            callback.onFailure(ex);
        }
    }


    /**
     * Send a message with text
     * @param productId
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @param text
     * @param callback
     */
    public void sendMessage(String productId, String customerNumber, Channel channel, String channelNumber, String text,  Callback<ChatResponse> callback) {
        sendMessage(productId, customerNumber, channel, channelNumber, new TextMessageBody(text), callback);
    }


    /**
     * Send a message with media
     * @param productId
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @param mediaType
     * @param url
     * @param callback
     */
    public void sendMessage(String productId, String customerNumber, Channel channel, String channelNumber, MediaType mediaType, String url, Callback<ChatResponse> callback) {
        sendMessage(productId, customerNumber, channel, channelNumber, new MediaMessageBody(mediaType.name(), url), callback);
    }

    /**
     * Send a message with location
     * @param productId
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @param latitude
     * @param longitude
     * @param callback
     */
    public void sendMessage(String productId, String customerNumber, Channel channel, String channelNumber, float latitude, float longitude, Callback<ChatResponse> callback) {
        sendMessage(productId, customerNumber, channel, channelNumber, new LocationMessageBody(latitude, longitude), callback);
    }


    /**
     * Send a template
     * @param productId
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @param template
     * @param callback
     */
    public void sendTemplate(String productId, String customerNumber, Channel channel, String channelNumber, Template template,  Callback<ChatResponse> callback) {
        sendMessage(productId, customerNumber, channel, channelNumber, new TextMessageTemplate(template), callback);
    }



    /* -> Consent */

    /**
     * Send consent
     * <p>
     *      Synchronously send the request and return its response.
     * </p>
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @param optIn
     * @return
     * @throws IOException
     */
    public ConsentResponse sendConsent(String customerNumber, Channel channel, String channelNumber, boolean optIn) throws IOException {
        checkPhoneNumber(customerNumber);
        Consent consent = new Consent(mUsername, customerNumber, channel.name().toLowerCase(), channelNumber, optIn ? "OptIn" : "OptOut");
        Response<ConsentResponse> resp = chat.sendConsent(consent).execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }


    /**
     * Send an opt-in consent
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @return
     * @throws IOException
     */
    public ConsentResponse optIn(String customerNumber, Channel channel, String channelNumber) throws IOException {
        return sendConsent(customerNumber, channel, channelNumber, true);
    }


    /**
     * Send an opt-out consent
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @return
     * @throws IOException
     */
    public ConsentResponse optOut(String customerNumber, Channel channel, String channelNumber) throws IOException {
        return sendConsent(customerNumber, channel, channelNumber, false);
    }

    /**
     * Send consent
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     *     occurred
     * </p>
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @param optIn
     * @param callback
     */
    public void sendConsent(String customerNumber, Channel channel, String channelNumber, boolean optIn, final Callback<ConsentResponse> callback) {
        try {
            checkPhoneNumber(customerNumber);
            Consent consent = new Consent(mUsername, customerNumber, channel.name().toLowerCase(), channelNumber, optIn ? "OptIn" : "OptOut");
            chat.sendConsent(consent).enqueue(makeCallback(new Callback<ConsentResponse>() {
                @Override
                public void onSuccess(ConsentResponse data) {
                    callback.onSuccess(data);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            }));

        } catch (IOException ex) {
            callback.onFailure(ex);
        }
    }

    /**
     * Sent an opt-in consent
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     *     occurred
     * </p>
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @param callback
     */
    public void optIn(String customerNumber, Channel channel, String channelNumber, final Callback<ConsentResponse> callback) {
        sendConsent(customerNumber, channel, channelNumber, true, callback);
    }


    /**
     * Send an opt-out consent
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     *     occurred
     * </p>
     * @param customerNumber
     * @param channel
     * @param channelNumber
     * @param callback
     */
    public void optOut(String customerNumber, Channel channel, String channelNumber, final Callback<ConsentResponse> callback) {
        sendConsent(customerNumber, channel, channelNumber, false, callback);
    }
}
