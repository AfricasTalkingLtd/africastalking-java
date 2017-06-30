package com.africastalking;

import com.africastalking.proto.Base;
import com.africastalking.proto.sms.RemoteSMSGrpc.*;
import com.africastalking.proto.sms.RemoteSMSOuterClass.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class RemoteSMSService extends RemoteSMSImplBase implements IAuthenticator {

    private static SMSService service;

    RemoteSMSService() {
        service = AfricasTalking.getService(SMSService.class);
    }


    private String getSenderId(SendRequest request) {
        String senderId = request.getFrom();
        if (senderId.compareToIgnoreCase("") == 0) { return null; }
        return senderId;
    }

    @Override
    public boolean isValidToken(Base.Token token, StreamObserver<Base.Response> responseObserver) {
        if (!ATServer.authenticate(token.getId())){
            responseObserver.onError(Status.UNAUTHENTICATED.asException());
            return false;
        }
        return true;
    }

    @Override
    public void fetchMessage(MessagesRequest request, final StreamObserver<Base.Response> responseObserver) {
        if (!isValidToken(request.getToken(), responseObserver)) { return; }

        service.fetchMessage(request.getLastReceivedId(), new Callback<String>() {
            @Override
            public void onSuccess(String data) {
                Base.Response resp = Base.Response.newBuilder().setResponse(data).build();
                responseObserver.onNext(resp);
                responseObserver.onCompleted();
            }

            @Override
            public void onFailure(Throwable throwable) {
                responseObserver.onError(throwable);
            }
        });
    }

    @Override
    public void fetchSubscription(SubscriptionRequest request, final StreamObserver<Base.Response> responseObserver) {
        if (!isValidToken(request.getToken(), responseObserver)) { return; }

        service.fetchSubscription(request.getShortCode(), request.getKeyword(), request.getLastReceivedId(), new Callback<String>() {
            @Override
            public void onSuccess(String data) {
                Base.Response resp = Base.Response.newBuilder().setResponse(data).build();
                responseObserver.onNext(resp);
                responseObserver.onCompleted();
            }

            @Override
            public void onFailure(Throwable throwable) {
                responseObserver.onError(throwable);
            }
        });
    }

    @Override
    public void createSubscription(SubscriptionRequest request, final StreamObserver<Base.Response> responseObserver) {
        if (!isValidToken(request.getToken(), responseObserver)) { return; }

        service.createSubscription(request.getShortCode(), request.getKeyword(), request.getPhoneNumber(), new Callback<String>() {
            @Override
            public void onSuccess(String data) {
                Base.Response resp = Base.Response.newBuilder().setResponse(data).build();
                responseObserver.onNext(resp);
                responseObserver.onCompleted();
            }

            @Override
            public void onFailure(Throwable throwable) {
                responseObserver.onError(throwable);
            }
        });
    }

    @Override
    public void send(SendRequest request, final StreamObserver<Base.Response> responseObserver) {
        if (!isValidToken(request.getToken(), responseObserver)) { return; }

        String[] recipients = new String[request.getRecipientsCount()];
        for(int i = 0; i < recipients.length; i++) {
            recipients[i] = request.getRecipients(i);
        }

        service.send(request.getMessage(), getSenderId(request), recipients, new Callback<String>() {
            @Override
            public void onSuccess(String data) {
                Base.Response resp = Base.Response.newBuilder().setResponse(data).build();
                responseObserver.onNext(resp);
                responseObserver.onCompleted();
            }

            @Override
            public void onFailure(Throwable throwable) {
                responseObserver.onError(throwable);
            }
        });
    }

    @Override
    public void sendBulk(SendRequest request, final StreamObserver<Base.Response> responseObserver) {
        if (!isValidToken(request.getToken(), responseObserver)) { return; }

        String[] recipients = new String[request.getRecipientsCount()];
        for(int i = 0; i < recipients.length; i++) {
            recipients[i] = request.getRecipients(i);
        }
        service.sendBulk(request.getMessage(), getSenderId(request), request.getEnqueue(), recipients, new Callback<String>() {
            @Override
            public void onSuccess(String data) {
                Base.Response resp = Base.Response.newBuilder().setResponse(data).build();
                responseObserver.onNext(resp);
                responseObserver.onCompleted();
            }

            @Override
            public void onFailure(Throwable throwable) {
                responseObserver.onError(throwable);
            }
        });
    }

    @Override
    public void sendPremium(SendRequest request, final StreamObserver<Base.Response> responseObserver) {
        if (!isValidToken(request.getToken(), responseObserver)) { return; }

        String[] recipients = new String[request.getRecipientsCount()];
        for(int i = 0; i < recipients.length; i++) {
            recipients[i] = request.getRecipients(i);
        }
        service.sendPremium(request.getMessage(), getSenderId(request), request.getKeyword(),
                request.getLinkId(), request.getRetryDurationInHours(), recipients, new Callback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        Base.Response resp = Base.Response.newBuilder().setResponse(data).build();
                        responseObserver.onNext(resp);
                        responseObserver.onCompleted();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        responseObserver.onError(throwable);
                    }
        });
    }
}
