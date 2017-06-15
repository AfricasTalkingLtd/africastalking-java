package com.africastalking;

import com.africastalking.proto.payment.RemotePaymentGrpc;
import com.africastalking.proto.payment.RemotePaymentGrpc.*;
import com.africastalking.proto.payment.RemotePaymentOuterClass.*;
import com.africastalking.recipient.Business;
import com.africastalking.recipient.Consumer;
import io.grpc.ManagedChannel;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public final class Payment extends PaymentsService {

    RemotePaymentBlockingStub blockingStub;
    RemotePaymentStub asyncStub;


    Payment() {
        ManagedChannel channel = RpcClient.getChannel();
        blockingStub = RemotePaymentGrpc.newBlockingStub(channel);
        asyncStub = RemotePaymentGrpc.newStub(channel);
    }


    @Override
    public String checkout(String productName, String phoneNumber, float amount, Currency currency, Map metadata) throws IOException {
        return super.checkout(productName, phoneNumber, amount, currency, metadata);
    }

    @Override
    public String payConsumers(String product, List<Consumer> recipients) throws IOException {
        return super.payConsumers(product, recipients);
    }

    @Override
    public void payBusiness(String product, Business recipient, Callback<String> callback) {
        super.payBusiness(product, recipient, callback);
    }
}
