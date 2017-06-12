package com.africastalking;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import com.africastalking.RemoteAccountGrpc.*;
import com.africastalking.RemoteAccountOuterClass.*;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class Account extends AccountService {

    ManagedChannel channel;
    RemoteAccountBlockingStub blockingStub;
    RemoteAccountStub asyncStub;

    Account(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true));
    }

    Account(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = RemoteAccountGrpc.newBlockingStub(channel);
        asyncStub = RemoteAccountGrpc.newStub(channel);

    }

    @Override
    public String getUser() throws IOException {
        AccountRequest req = AccountRequest.newBuilder().setId(System.currentTimeMillis()).build();
        BaseResponse resp = blockingStub.getUser(req);
        return resp.getResponse();
    }

    @Override
    public void getUser(final Callback<String> callback) {
        AccountRequest req = AccountRequest.newBuilder().setId(System.currentTimeMillis()).build();
        asyncStub.getUser(req, new StreamObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse value) {
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
