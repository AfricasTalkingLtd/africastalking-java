package com.africastalking;

import com.africastalking.application.ApplicationResponse;

import java.io.IOException;

public final class AT {

    private static void log(String message) {
        System.out.println(message);
    }

    public static void main(String[] argv) {
        log("\nAfrica's Talking SDK\n");
        AfricasTalking.initialize(argv[0], argv[1]);
        try {
            log("\tGetting app account info...\n");
            ApplicationResponse resp = AfricasTalking.getService(ApplicationService.class).fetchApplicationData();
            log("\tBalance: " + resp.userData.balance);
        } catch (IOException e) {
            System.out.println("Failed to get info!");
        }
    }
}