package com.africastalking;

import java.io.IOException;

public class SimpleServer {

    public static void main(String[] argv) throws IOException, InterruptedException {
        Logger logger = new BaseLogger();
        logger.log("\nAfrica's Talking gRPC Server\n");
        RpcServer server = new RpcServer(argv[0], argv[1]);
        server.start();
        server.blockUntilShutdown();
    }
}
