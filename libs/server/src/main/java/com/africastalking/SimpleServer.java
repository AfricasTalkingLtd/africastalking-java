package com.africastalking;

import java.io.IOException;

public class SimpleServer {

    public static void main(String[] argv) throws IOException, InterruptedException {
        Logger logger = new BaseLogger();
        logger.log("\nAfrica's Talking gRPC Server\n");
        ATServer server = new ATServer(argv[0], argv[1]);

        logger.log("Sample Tokens: ");
        int tokenCount = 5;
        try {
            tokenCount = Integer.parseInt(argv[2]);
        } catch (NumberFormatException ex) { /* ignore */ }

        for (int i = 0; i < tokenCount; i++) {
            logger.log("\t%s\n", server.generateToken());
        }

        server.start();
        server.blockUntilShutdown();
    }
}
