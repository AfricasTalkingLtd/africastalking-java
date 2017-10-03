package com.africastalking;

import com.africastalking.utils.Networking;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.hashids.Hashids;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.UUID;

public class ATServer {

    private static final int DEFAULT_PORT = 59123;
    private static final Logger LOGGER = new Logger(){
        @Override
        public void log(String message, Object... args) {
            System.out.println(String.format(message, args));
        }
    };

    private Server server;
    private static HashSet<String> tokenStore = new HashSet<>(); // FIXME: Find a scalable solution

    private static Hashids hashids = new Hashids(UUID.randomUUID().toString());

    public ATServer(String username, String apiKey) {
        AfricasTalking.initialize(username, apiKey);
        AfricasTalking.setLogger(LOGGER);
    }

    public void start() throws IOException {
        this.start(DEFAULT_PORT, null, null);
    }


    public void start(int port, File certChainFile, File privateKeyFile) throws IOException {

        ServerBuilder builder = ServerBuilder.forPort(port)
                .addService(new RemoteAirtimeService())
                .addService(new RemotePaymentService())
                .addService(new RemoteSMSService())
                .addService(new RemoteAirtimeService());

        if (certChainFile != null && privateKeyFile != null) {
            builder.useTransportSecurity(certChainFile, privateKeyFile);
        }

        server = builder.build();


        server.start();
        Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
        LOGGER.log("Server started...");

        InetAddress inetAddress = Networking.getDefaultInetAddress();
        if (inetAddress != null) {
            LOGGER.log("\t%s:%s\n", inetAddress.getHostAddress(), port);
        } else {
            LOGGER.log("\tListening on port %d", port);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may has been reset by its JVM shutdown hook.
                System.err.println("*** shutting down server since JVM is shutting down");
                ATServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    /** Stop serving requests and shutdown resources. */
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * CHeck a token's validity
     * @param token String
     * @return authenticated boolean
     */
    static boolean authenticate(String token) {
        return tokenStore.contains(token);
    }

    /**
     * Generate a new auth token to be sent securely, out-of-band to the client
     * @return token String
     */
    public String generateToken() {
        String token = hashids.encode(System.currentTimeMillis());
        tokenStore.add(token);
        return token;
    }

    /**
     * Revoke token
     * @param token
     * @return
     */
    public boolean revokeToken(String token) {
        tokenStore.remove(token);
        return true;
    }

}
