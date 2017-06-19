package com.africastalking;

import com.africastalking.proto.Base;
import com.africastalking.proto.payment.RemotePaymentGrpc;
import com.africastalking.proto.payment.RemotePaymentGrpc.*;
import com.africastalking.proto.payment.RemotePaymentOuterClass.*;
import com.africastalking.payments.recipient.Business;
import com.africastalking.payments.recipient.Consumer;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Payment {

    private RemotePaymentBlockingStub blockingStub;
    private RemotePaymentStub asyncStub;


    Payment() {
        ManagedChannel channel = ATClient.getChannel();
        blockingStub = RemotePaymentGrpc.newBlockingStub(channel);
        asyncStub = RemotePaymentGrpc.newStub(channel);
    }


    public String checkout(String productName, String phoneNumber, float amount, Currency currency, Map metadata) throws IOException {

        if (metadata == null) {
            metadata = new HashMap();
        }

        CheckoutRequest request = CheckoutRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN).build())
                .setProductName(productName)
                .setPhoneNumber(phoneNumber)
                .setAmount(amount)
                .setCurrency(Base.Currency.newBuilder().setText(currency.toString()).build())
                .putAllMeta(metadata)
                .build();

        Base.Response response = blockingStub.checkout(request);
        return response.getResponse();
    }

    public void checkout(String productName, String phoneNumber, float amount, Currency currency, Map metadata, final Callback<String> callback) {

        if (metadata == null) {
            metadata = new HashMap();
        }

        CheckoutRequest request = CheckoutRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN).build())
                .setProductName(productName)
                .setPhoneNumber(phoneNumber)
                .setAmount(amount)
                .setCurrency(Base.Currency.newBuilder().setText(currency.toString()).build())
                .putAllMeta(metadata)
                .build();

        asyncStub.checkout(request, new StreamObserver<Base.Response>() {
            @Override
            public void onNext(Base.Response value) {
                callback.onSuccess(value.getResponse());
            }

            @Override
            public void onError(Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onCompleted() {}
        });
    }

    public String payConsumers(String product, List<Consumer> recipients) throws IOException {

        List<Base.Consumer> consumers = new ArrayList<>();
        for (Consumer recipient:recipients) {
            Base.Consumer consumer = Base.Consumer.newBuilder()
                    .setAmount(recipient.amount)
                    .setPhoneNumber(recipient.phoneNumber)
                    .setReason(recipient.reason.toString())
                    .putAllMetadata(recipient.metadata)
                    .setCurrency(Base.Currency.newBuilder().setText(recipient.currencyCode.toString()).build())
                    .build();
            consumers.add(consumer);
        }

        B2CRequest request = B2CRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN).build())
                .setProductName(product)
                .addAllRecipients(consumers)
                .build();

        Base.Response response = blockingStub.payCustomers(request);
        return response.getResponse();
    }

    public void payConsumers(String product, List<Consumer> recipients, final Callback<String> callback) {
        List<Base.Consumer> consumers = new ArrayList<>();
        for (Consumer recipient:recipients) {
            Base.Consumer consumer = Base.Consumer.newBuilder()
                    .setAmount(recipient.amount)
                    .setPhoneNumber(recipient.phoneNumber)
                    .setReason(recipient.reason.toString())
                    .putAllMetadata(recipient.metadata)
                    .setCurrency(Base.Currency.newBuilder().setText(recipient.currencyCode.toString()).build())
                    .build();
            consumers.add(consumer);
        }

        B2CRequest request = B2CRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN).build())
                .setProductName(product)
                .addAllRecipients(consumers)
                .build();

        asyncStub.payCustomers(request, new StreamObserver<Base.Response>() {
            @Override
            public void onNext(Base.Response value) {
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

    public String payBusiness(String product, Business recipient) throws IOException {

        Base.Business business = Base.Business.newBuilder()
                .setDestinationAccount(recipient.destinationAccount)
                .setDestinationChannel(recipient.destinationChannel)
                .setTransferType(recipient.transferType)
                .setAmount(recipient.amount)
                .setCurrency(Base.Currency.newBuilder().setText(recipient.currencyCode.toString()).build())
                .build();

        B2BRequest request = B2BRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN).build())
                .setProductName(product)
                .setRecipient(business)
                .build();

        Base.Response response = blockingStub.payBusiness(request);
        return response.getResponse();
    }

    public void payBusiness(String product, Business recipient, final Callback<String> callback) {

        Base.Business business = Base.Business.newBuilder()
                .setDestinationAccount(recipient.destinationAccount)
                .setDestinationChannel(recipient.destinationChannel)
                .setTransferType(recipient.transferType)
                .setAmount(recipient.amount)
                .setCurrency(Base.Currency.newBuilder().setText(recipient.currencyCode.toString()).build())
                .build();

        B2BRequest request = B2BRequest.newBuilder()
                .setToken(Base.Token.newBuilder().setId(ATClient.TOKEN).build())
                .setProductName(product)
                .setRecipient(business)
                .build();

        asyncStub.payBusiness(request, new StreamObserver<Base.Response>() {
            @Override
            public void onNext(Base.Response value) {
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
