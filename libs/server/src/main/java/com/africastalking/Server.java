package com.africastalking;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerBuilder;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.Status;
import io.grpc.ServerCall.Listener;

import java.io.File;
import java.io.IOException;
import java.lang.IllegalStateException;
import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

public final class Server {

    private static int DEFAULT_PORT = 35897;

    private static final Metadata.Key<String> CLIENT_ID_HEADER_KEY = Metadata.Key.of("X-Client-Id", ASCII_STRING_MARSHALLER);
    private static final Context.Key<String> CLIENT_ID_CONTEXT_KEY = Context.key("X-Client-Id");

    private io.grpc.Server mGrpc;
    private SdkServerService mSdkService;
    private Authenticator mAuthenticator = null;

    public Server() throws IllegalStateException {
        if (AfricasTalking.sUsername == null || AfricasTalking.sApiKey == null) {
            throw new IllegalStateException("You must call Africastalking.initialize() before making a server");
        }
        mSdkService = new SdkServerService();
    }

    public Server(Authenticator authenticator) throws IllegalStateException {
        this();
        mAuthenticator = authenticator;
    }

    public void addSipCredentials(String username, String password, String host, int port, String transport) {
        mSdkService.addSipCredentials(username, password, host, port, transport);
    }

    public void addSipCredentials(String username, String password, String host) {
        this.addSipCredentials(username, password, host, 5060, "udp");
    }

    public void start(File certChainFile, File privateKeyFile, int port) throws IOException {
        ServerBuilder builder = ServerBuilder.forPort(port).useTransportSecurity(certChainFile, privateKeyFile);
        if (mAuthenticator != null) {
            builder.addService(ServerInterceptors.intercept(
                mSdkService, new AuthenticationInterceptor(this.mAuthenticator)));
        } else {
            builder.addService(mSdkService);
        }
        mGrpc = builder.build();
        mGrpc.start();
    }

    public void start(File certChainFile, File privateKeyFile) throws IOException {
        this.start(certChainFile, privateKeyFile, DEFAULT_PORT);
    }

    public void startInsecure(int port) throws IOException {
        ServerBuilder builder = ServerBuilder.forPort(port);
        if (mAuthenticator != null) {
            builder.addService(ServerInterceptors.intercept(
                mSdkService, new AuthenticationInterceptor(this.mAuthenticator)));
        } else {
            builder.addService(mSdkService);
        }
        mGrpc = builder.build();
        mGrpc.start();
    }

    public void startInsecure() throws IOException {
        startInsecure(DEFAULT_PORT);
    }

    private class AuthenticationInterceptor implements ServerInterceptor {
        final Listener NOOP_LISTENER = new Listener() {};
        
        Authenticator authenticator;

        AuthenticationInterceptor(Authenticator authenticator) {
            this.authenticator = authenticator;
        }

        @Override
        public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
            String clientId = headers.get(CLIENT_ID_HEADER_KEY);
            if (clientId == null || !authenticator.authenticate(clientId)) {
                call.close(Status.UNAUTHENTICATED.withDescription("Invalid or unknown client: " + clientId), headers);
                return NOOP_LISTENER;
            }
            Context context = Context.current().withValue(CLIENT_ID_CONTEXT_KEY, clientId);
            return Contexts.interceptCall(context, call, headers, next);
        }
    }
}