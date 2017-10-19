package com.africastalking;

import com.africastalking.proto.Base;
import com.africastalking.proto.airtime.RemoteAirtimeGrpc.*;
import com.africastalking.proto.airtime.RemoteAirtimeOuterClass.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;

public class RemoteAirtimeService extends RemoteAirtimeImplBase implements IAuthenticator {

    private static AirtimeService service;

    RemoteAirtimeService() {
        service = AfricasTalking.getService(AirtimeService.class);
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
    public void send(AirtimeRequest request, final StreamObserver<Base.Response> responseObserver) {
        if (!isValidToken(request.getToken(), responseObserver)) { return; }

        HashMap<String, String> recipients = new HashMap<>(request.getRecipientsMap());
        service.send(recipients, new Callback<String>() {
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
