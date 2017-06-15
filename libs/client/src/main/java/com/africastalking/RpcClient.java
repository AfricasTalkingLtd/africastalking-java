package com.africastalking;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class RpcClient {

    static String HOST;
    static int PORT;
    static String TOKEN;

    private static ManagedChannel CHANNEL;

    private static Account account;
    private static Payment payment;
//    private static SMS sms;
//    private static USSD ussd;
//    private static Voice voice;

    public static void initialize(String host, int port, String token) {
        HOST = host;
        PORT = port;
        TOKEN = token;
    }
    static ManagedChannel getChannel() {
        if (CHANNEL == null) {
            ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder
                    .forAddress(HOST, PORT)
                    .usePlaintext(true); // FIXME: Remove to Setup TLS
            CHANNEL = channelBuilder.build();
        }
        return CHANNEL;
    }

    public static Account getAccountService() {
        if (account == null) {
            account = new Account();
        }
        return account;
    }

    public static Payment getPaymentService() {
        if (payment == null) {
            payment = new Payment();
        }
        return payment;
    }

}
