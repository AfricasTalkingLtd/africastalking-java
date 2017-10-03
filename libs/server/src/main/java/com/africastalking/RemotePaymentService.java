package com.africastalking;

import com.africastalking.proto.Base;
import com.africastalking.proto.payment.RemotePaymentGrpc.*;
import com.africastalking.proto.payment.RemotePaymentOuterClass.*;
import com.africastalking.payments.recipient.*;
import com.africastalking.payments.recipient.Consumer;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RemotePaymentService extends RemotePaymentImplBase implements IAuthenticator {

    private static PaymentService service;


    RemotePaymentService() {
        service = AfricasTalking.getService(PaymentService.class);
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
    public void checkout(CheckoutRequest request, final StreamObserver<Base.Response> responseObserver) {
        if (!isValidToken(request.getToken(), responseObserver)) { return; }

        String productName = request.getProductName();
        String phoneNumber = request.getPhoneNumber();
        String amount = request.getAmount();
        Map<String, String> meta = request.getMetaMap();
        service.checkout(productName, phoneNumber, amount, meta, new Callback<String>() {
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
    public void payBusiness(B2BRequest request, final StreamObserver<Base.Response> responseObserver) {
        if (!isValidToken(request.getToken(), responseObserver)) { return; }

        String product = request.getProductName();
        Base.Business recipient = request.getRecipient();

        Business business = new Business(
                recipient.getDestinationChannel(),
                recipient.getDestinationAccount(),
                Business.TransferType.valueOf(recipient.getTransferType()),
                recipient.getCurrencyCode() + " " + recipient.getAmount()
        );
        service.payBusiness(product, business, new Callback<String>() {
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
    public void payCustomers(B2CRequest request, final StreamObserver<Base.Response> responseObserver) {
        if (!isValidToken(request.getToken(), responseObserver)) { return; }

        String product = request.getProductName();
        List<Base.Consumer> recipients = request.getRecipientsList();
        List<Consumer> consumers = new ArrayList<>();
        for (Base.Consumer recipient: recipients) {
            Consumer consumer = new Consumer(
                    recipient.getName(),
                    recipient.getPhoneNumber(),
                    recipient.getCurrencyCode() + " " + recipient.getAmount()
            );
            consumer.providerChannel = recipient.getProviderChannel();
            consumer.reason = Consumer.Reason.valueOf(recipient.getReason());
            consumer.metadata = new HashMap<>(recipient.getMetadataMap());
            consumers.add(consumer);
        }


        service.payConsumers(product, consumers, new Callback<String>() {
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
