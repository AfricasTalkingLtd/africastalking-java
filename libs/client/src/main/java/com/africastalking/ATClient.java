package com.africastalking;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ATClient {

    private static String HOST;
    private static int PORT = -1;
    static String TOKEN;

    private static ManagedChannel CHANNEL;

    private static Account account;
    private static Payment payment;
    private static SMS sms;
    private static Airtime airtime;
//    private static USSD ussd;
//    private static Voice voice;

    public static void initialize(String host, int port, String token) {
        HOST = host;
        PORT = port;
        TOKEN = token;
        CHANNEL = null;
    }

    static ManagedChannel getChannel() {
        if (HOST == null || PORT == -1) throw new RuntimeException("call ATClient.initialize(host, port, token) first");
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

    public static SMS getSmsService() {
        if (sms == null) {
            sms = new SMS();
        }
        return sms;
    }

    public static Airtime getAirtimeService() {
        if (airtime == null) {
            airtime = new Airtime();
        }
        return airtime;
    }

}
