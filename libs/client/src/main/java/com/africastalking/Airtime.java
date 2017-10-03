package com.africastalking;

import com.africastalking.proto.Base;
import com.africastalking.proto.airtime.RemoteAirtimeGrpc;
import com.africastalking.proto.airtime.RemoteAirtimeGrpc.*;
import com.africastalking.proto.airtime.RemoteAirtimeOuterClass.*;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.HashMap;

public final class Airtime {

    private RemoteAirtimeBlockingStub blockingStub;
    private RemoteAirtimeStub asyncStub;

    Airtime() {
        ManagedChannel channel = ATClient.getChannel();
        blockingStub = RemoteAirtimeGrpc.newBlockingStub(channel);
        asyncStub = RemoteAirtimeGrpc.newStub(channel);
    }

    public String send(String phone, String amount) throws IOException {
        HashMap<String, String> recipient = new HashMap<>();
        recipient.put(phone, amount);
        return send(recipient);
    }

    public void send(String phone, String amount, Callback<String> callback) {
        HashMap<String, String> recipient = new HashMap<>();
        recipient.put(phone, amount);
        send(recipient, callback);
    }

    public String send(HashMap<String, String> recipients) throws IOException {
        AirtimeRequest req = AirtimeRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN).build())
                .putAllRecipients(recipients)
                .build();
        Base.Response res = blockingStub.send(req);
        return res.getResponse();
    }

    public void send(HashMap<String, String> recipients, final Callback<String> callback) {
        AirtimeRequest req = AirtimeRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN).build())
                .putAllRecipients(recipients)
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
}
