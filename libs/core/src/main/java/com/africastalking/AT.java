package com.africastalking;

import java.io.IOException;

class AT {

    private static void log(String message) {
        System.out.println(message);
    }

    public static void main(String[] argv) {
        log("\nAfrica's Talking SDK\n");
        AfricasTalking.initialize(argv[0], argv[1], Format.XML);
        try {
            log("\tgetting user account...\n");
            String user = AfricasTalking.getService(AccountService.class).getUser();
            log("\t" + user);
        } catch (IOException e) {
            System.out.println("Failed to get AT account!");
        }
    }

}
