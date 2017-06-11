package com.africastalking;

import java.io.IOException;

class AT {

    private static Logger logger = new BaseLogger();

    public static void main(String[] argv) throws IOException, InterruptedException {
        logger.log("\nAfrica's Talking SDK: Demo RPC Server\n");

        String username = "at2fa";
        String apiKey = "8c940cd77db666ca100e9dd0d784191ada2ee3eaa1d0a952170a68595313f4ab";

        RpcServer server = new RpcServer(8980, username, apiKey, Format.JSON, Environment.SANDBOX, logger);
        server.start();
        server.blockUntilShutdown();
    }

}
