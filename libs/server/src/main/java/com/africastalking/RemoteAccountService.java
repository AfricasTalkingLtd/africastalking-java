package com.africastalking;

import com.africastalking.RemoteAccountGrpc.*;
import com.africastalking.RemoteAccountOuterClass.*;

import io.grpc.stub.StreamObserver;

public class RemoteAccountService extends RemoteAccountImplBase {

    private static AccountService service;
    private static Logger logger = new BaseLogger();

    RemoteAccountService() {
        service = AfricasTalking.getService(AccountService.class);
    }

    @Override
    public void getUser(AccountRequest request, final StreamObserver<BaseResponse> responseObserver) {
        service.getUser(new Callback<String>() {
            @Override
            public void onSuccess(String data) {
                BaseResponse resp = BaseResponse.newBuilder().setResponse(data).build();
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
