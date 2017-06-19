package com.africastalking;

import com.africastalking.proto.Base;
import com.africastalking.proto.sms.RemoteSMSGrpc;
import com.africastalking.proto.sms.RemoteSMSGrpc.*;
import com.africastalking.proto.sms.RemoteSMSOuterClass.*;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class SMS {

    private RemoteSMSBlockingStub blockingStub;
    private RemoteSMSStub asyncStub;

    SMS() {
        ManagedChannel channel = ATClient.getChannel();
        blockingStub = RemoteSMSGrpc.newBlockingStub(channel);
        asyncStub = RemoteSMSGrpc.newStub(channel);
    }

    public String fetchMessage(String lastReceivedId) throws IOException {
        Base.Response response =  blockingStub.fetchMessage(MessagesRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN).build())
                .setLastReceivedId(lastReceivedId).build());
        return response.getResponse();
    }

    public void fetchMessage(String lastReceivedId, final Callback<String> callback) {
        MessagesRequest req = MessagesRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN))
                .setLastReceivedId(lastReceivedId).build();
        asyncStub.fetchMessage(req, new StreamObserver<Base.Response>() {
            @Override
            public void onNext(Base.Response value) {
                callback.onSuccess(value.getResponse());
            }

            @Override
            public void onError(Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onCompleted() {

            }
        });

    }

    public String fetchSubscription(String shortCode, String keyword, String lastReceivedId) throws IOException {

        SubscriptionRequest req = SubscriptionRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN))
                .setShortCode(shortCode)
                .setKeyword(keyword)
                .setLastReceivedId(lastReceivedId)
                .build();

        Base.Response resp = blockingStub.fetchSubscription(req);
        return resp.getResponse();
    }

    public void fetchSubscription(String shortCode, String keyword, String lastReceivedId, final Callback<String> callback) {

        SubscriptionRequest req = SubscriptionRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN))
                .setShortCode(shortCode)
                .setKeyword(keyword)
                .setLastReceivedId(lastReceivedId)
                .build();

        asyncStub.fetchSubscription(req, new StreamObserver<Base.Response>() {
            @Override
            public void onNext(Base.Response value) {
                callback.onSuccess(value.getResponse());
            }

            @Override
            public void onError(Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onCompleted() {

            }
        });
    }

    public String createSubscription(String shortCode, String keyword, String phoneNumber) throws IOException {
        SubscriptionRequest req = SubscriptionRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN))
                .setShortCode(shortCode)
                .setKeyword(keyword)
                .setPhoneNumber(phoneNumber)
                .build();

        Base.Response resp = blockingStub.createSubscription(req);
        return resp.getResponse();
    }

    public void createSubscription(String shortCode, String keyword, String phoneNumber, final Callback<String> callback) {
        SubscriptionRequest req = SubscriptionRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN))
                .setShortCode(shortCode)
                .setKeyword(keyword)
                .setPhoneNumber(phoneNumber)
                .build();

        asyncStub.fetchSubscription(req, new StreamObserver<Base.Response>() {
            @Override
            public void onNext(Base.Response value) {
                callback.onSuccess(value.getResponse());
            }

            @Override
            public void onError(Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onCompleted() {

            }
        });
    }

    public String send(String message, String[] recipients) throws IOException {
        return send(message, null, recipients);
    }

    public void send(String message, String[] recipients, Callback<String> callback) {
        send(message, null, recipients, callback);
    }

    public String send(String message, String from, String[] recipients) throws IOException {

        List<String> to = Arrays.asList(recipients);
        SendRequest req = SendRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN).build())
                .setMessage(message)
                .setFrom(from)
                .addAllRecipients(to)
                .build();

        Base.Response res = blockingStub.send(req);
        return res.getResponse();
    }

    public void send(String message, String from, String[] recipients, final Callback<String> callback) {

        List<String> to = Arrays.asList(recipients);
        SendRequest req = SendRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN).build())
                .setMessage(message)
                .setFrom(from)
                .addAllRecipients(to)
                .build();

        asyncStub.send(req, new StreamObserver<Base.Response>() {
            @Override
            public void onNext(Base.Response value) {
                callback.onSuccess(value.getResponse());
            }

            @Override
            public void onError(Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onCompleted() {

            }
        });

    }

    public String sendBulk(String message, String from, boolean enqueue, String[] recipients) throws IOException {
        List<String> to = Arrays.asList(recipients);
        SendRequest req = SendRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN).build())
                .setMessage(message)
                .setFrom(from)
                .setEnqueue(enqueue)
                .addAllRecipients(to)
                .build();

        Base.Response res = blockingStub.sendBulk(req);
        return res.getResponse();
    }

    public void sendBulk(String message, String from, boolean enqueue, String[] recipients, final Callback<String> callback) {

        List<String> to = Arrays.asList(recipients);
        SendRequest req = SendRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN).build())
                .setMessage(message)
                .setFrom(from)
                .setEnqueue(enqueue)
                .addAllRecipients(to)
                .build();

        asyncStub.sendBulk(req, new StreamObserver<Base.Response>() {
            @Override
            public void onNext(Base.Response value) {
                callback.onSuccess(value.getResponse());
            }

            @Override
            public void onError(Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onCompleted() {

            }
        });

    }

    public String sendPremium(String message, String from, String keyword, String linkId, long retryDurationInHours, String[] recipients) throws IOException {

        List<String> to = Arrays.asList(recipients);
        SendRequest req = SendRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN).build())
                .setMessage(message)
                .setFrom(from)
                .addAllRecipients(to)
                .setKeyword(keyword)
                .setLinkId(linkId)
                .setRetryDurationInHours(retryDurationInHours)
                .build();

        Base.Response res = blockingStub.sendPremium(req);
        return res.getResponse();

    }

    public void sendPremium(String message, String from, String keyword, String linkId, long retryDurationInHours, String[] recipients, final Callback<String> callback) {

        List<String> to = Arrays.asList(recipients);
        SendRequest req = SendRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN))
                .setMessage(message)
                .setFrom(from)
                .addAllRecipients(to)
                .setKeyword(keyword)
                .setLinkId(linkId)
                .setRetryDurationInHours(retryDurationInHours)
                .build();

        asyncStub.sendPremium(req, new StreamObserver<Base.Response>() {
            @Override
            public void onNext(Base.Response value) {
                callback.onSuccess(value.getResponse());
            }

            @Override
            public void onError(Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onCompleted() {

            }
        });
    }


}
