package com.africastalking;

public class RpcClient {

    private static String HOST;
    private static int PORT;

    private static Account account;

    public static void initialize(String host, int port) {
        HOST = host;
        PORT = port;
    }

    public static Account getAccountService() {
        if (account == null) {
            account = new Account(HOST, PORT);
        }
        return account;
    }
}
