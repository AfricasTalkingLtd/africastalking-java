package com.africastalking;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;

import java.io.IOException;

public class RpcServer {

    private int port = 59123;
    private Server server;

    static Logger LOGGER = new BaseLogger();

    public RpcServer(int port, String username, String apiKey, Format format, Environment environment, Logger logger) {
        AfricasTalking.initialize(username, apiKey, format);
        AfricasTalking.setEnvironment(environment);
        AfricasTalking.setLogger(logger);
        if (logger != null) {
            LOGGER = logger;
        }
        initServer(port);
    }


    private void initServer(int port) {
        this.port = port;
        server = ServerBuilder.forPort(port)
                .addService(new RemoteAccountService())
                .build();

    }

    public void start() throws IOException {
        server.start();
        LOGGER.log("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may has been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                RpcServer.this.stop();
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

}
