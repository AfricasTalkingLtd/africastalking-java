package com.africastalking;

import io.grpc.ManagedChannel;

import com.africastalking.proto.account.RemoteAccountGrpc;
import com.africastalking.proto.account.RemoteAccountGrpc.*;
import com.africastalking.proto.account.RemoteAccountOuterClass.*;
import com.africastalking.proto.Base.*;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public final class Account {

    private RemoteAccountBlockingStub blockingStub;
    private RemoteAccountStub asyncStub;


    Account() {
        ManagedChannel channel = AfricastalkingClient.getChannel();
        blockingStub = RemoteAccountGrpc.newBlockingStub(channel);
        asyncStub = RemoteAccountGrpc.newStub(channel);

    }

    public String getUser() throws IOException {
        AccountRequest req = AccountRequest.newBuilder().setToken(Token.newBuilder().setId(AfricastalkingClient.TOKEN)).build();
        Response resp = blockingStub.getUser(req);
        return resp.getResponse();
    }

    public void getUser(final Callback<String> callback) {
        AccountRequest req = AccountRequest.newBuilder().setToken(Token.newBuilder().setId(AfricastalkingClient.TOKEN)).build();
        asyncStub.getUser(req, new StreamObserver<Response>() {
            @Override
            public void onNext(Response value) {
                callback.onSuccess(value.getResponse());
            }

            @Override
            public void onError(Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onCompleted() { }
        });
    }
}
