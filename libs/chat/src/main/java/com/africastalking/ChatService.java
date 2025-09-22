package com.africastalking;


import com.africastalking.chat.*;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

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


    /* -> Message & Template <- */


    /**
     * Send a message
     * <p>
     *     Synchronously send the request and return its response.
     * </p>
     * @param customerNumber
     * @param channelNumber - e.g. WhatsApp number
     * @param body
     * @return
     * @throws IOException
     */
    public ChatResponse sendMessage(String customerNumber, String channelNumber, MessageBody body) throws IOException {
        ChatMessage message = new ChatMessage();
        message.body = body;
        message.username = mUsername;
        message.waNumber = channelNumber;
        message.phoneNumber = customerNumber;
        Response<ChatResponse> resp = chat.sendMessage(message).execute();
        if (!resp.isSuccessful()) {
            throw new IOException(resp.errorBody().string());
        }
        return resp.body();
    }


    /**
     * Send a message with text
     * @param customerNumber
     * @param channelNumber
     * @param text
     * @return
     * @throws IOException
     */
    public ChatResponse sendMessage(String customerNumber,  String channelNumber, String text) throws IOException {
        return sendMessage(customerNumber, channelNumber, new TextMessageBody(text));
    }

    /**
     * Send a message with media
     * @param customerNumber
     * @param channelNumber
     * @param mediaType
     * @param url
     * @return
     * @throws IOException
     */
    public ChatResponse sendMessage(String customerNumber, String channelNumber, MediaMessageBody.MediaType mediaType, String url) throws IOException {
        return sendMessage(customerNumber, channelNumber, new MediaMessageBody(mediaType, url));
    }

    /**
     * Send a template
     * <p>
     *  Synchronously send the request and return its response.
     * </p>
     * @param customerNumber
     * @param channelNumber
     * @param templateId
     * @param header
     * @param body
     * @return
     * @throws IOException
     */
    public ChatResponse sendTemplate(String customerNumber, String channelNumber, String templateId, String header, List<String> body) throws IOException {
        return sendMessage(customerNumber, channelNumber, new TemplateMessageBody(templateId, header, body));
    }


    /**
     * Send a message
     * <p>
     *     Asynchronously send the request and notify {@code callback} of its response or if an error
     *     occurred
     * </p>
     * @param customerNumber
     * @param channelNumber
     * @param body
     * @param callback
     */
    public void sendMessage(String customerNumber, String channelNumber, MessageBody body, Callback<ChatResponse> callback) {
        ChatMessage message = new ChatMessage();
        message.body = body;
        message.username = mUsername;
        message.waNumber = channelNumber;
        message.phoneNumber = customerNumber;

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
    }


    /**
     * Send a message with text
     * @param customerNumber
     * @param channelNumber
     * @param text
     * @param callback
     */
    public void sendMessage(String customerNumber, String channelNumber, String text,  Callback<ChatResponse> callback) {
        sendMessage(customerNumber, channelNumber, new TextMessageBody(text), callback);
    }


    /**
     * Send a message with media
     * @param customerNumber
     * @param channelNumber
     * @param mediaType
     * @param url
     * @param callback
     */
    public void sendMessage(String customerNumber, String channelNumber, MediaMessageBody.MediaType mediaType, String url, Callback<ChatResponse> callback) {
        sendMessage(customerNumber, channelNumber, new MediaMessageBody(mediaType, url), callback);
    }



    /**
     * Send a template message
     * @param customerNumber
     * @param channelNumber
     * @param templateId
     * @param header
     * @param body
     * @param callback
     */
    public void sendTemplate(String customerNumber, String channelNumber, String templateId, String header, List<String> body,  Callback<ChatResponse> callback) {
        sendMessage(customerNumber, channelNumber, new TemplateMessageBody(templateId, header, body), callback);
    }

}
