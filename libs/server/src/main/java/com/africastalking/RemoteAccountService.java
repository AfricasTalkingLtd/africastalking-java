package com.africastalking;

import com.africastalking.proto.account.RemoteAccountGrpc.*;
import com.africastalking.proto.account.RemoteAccountOuterClass.*;
import com.africastalking.proto.Base.*;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

class RemoteAccountService extends RemoteAccountImplBase implements IAuthenticator {

    private static AccountService service;

    RemoteAccountService() {
        service = AfricasTalking.getService(AccountService.class);
    }

    @Override
    public boolean isValidToken(Token token, StreamObserver<Response> responseObserver) {
        if (!ATServer.authenticate(token.getId())){
            responseObserver.onError(Status.UNAUTHENTICATED.asException());
            return false;
        }
        return true;
    }

    @Override
    public void getUser(AccountRequest request, final StreamObserver<Response> responseObserver) {
        if (!isValidToken(request.getToken(), responseObserver)) { return; }

        service.getUser(new Callback<String>() {
            @Override
            public void onSuccess(String data) {
                Response resp = Response.newBuilder().setResponse(data).build();
                responseObserver.onNext(resp);
                responseObserver.onCompleted();

            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                responseObserver.onError(throwable);
            }
        });
    }
}
