package com.africastalking.test.server;


import com.africastalking.AfricasTalking;
import com.africastalking.Authenticator;
import com.africastalking.test.Fixtures;
import com.africastalking.Server;
import com.africastalking.proto.SdkServerServiceGrpc;
import com.africastalking.proto.SdkServerServiceGrpc.SdkServerServiceBlockingStub;
import com.africastalking.proto.SdkServerServiceOuterClass.ClientTokenRequest;
import com.africastalking.proto.SdkServerServiceOuterClass.ClientTokenResponse;
import com.africastalking.proto.SdkServerServiceOuterClass.SipCredentials;
import com.africastalking.proto.SdkServerServiceOuterClass.SipCredentialsRequest;
import com.africastalking.proto.SdkServerServiceOuterClass.SipCredentialsResponse;

import io.grpc.Attributes;
import io.grpc.CallCredentials;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;

import org.junit.BeforeClass;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ATServerTest {

    final static int TEST_PORT = 9736;
    final static String TEST_CLIENT_ID = "TEST-ID-XXXX";
    static File certFile, privateKeyFile;

    static Server server;
    static SdkServerServiceBlockingStub client;

    @BeforeClass
    public static void startServer() throws IOException {

        ClassLoader loader = ClassLoader.getSystemClassLoader();
        certFile = new File(loader.getResource("cert/cert.pem").getFile());
        privateKeyFile = new File(loader.getResource("cert/key.pem").getFile());

        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
        server = new Server(new Authenticator() {
            @Override
            public boolean authenticate(String client) {
                return client.compareToIgnoreCase(TEST_CLIENT_ID) == 0;
            }
        });
        server.addSipCredentials("test", "secret", "sip://at.dev");
        server.start(certFile, privateKeyFile, TEST_PORT);
        
        ManagedChannel ch = NettyChannelBuilder.forAddress("localhost", TEST_PORT)
            .sslContext(GrpcSslContexts.forClient().trustManager(certFile).build())
            .build();

            
        client = SdkServerServiceGrpc.newBlockingStub(ch)
            .withCallCredentials(new CallCredentials(){
                @Override
                public void applyRequestMetadata(RequestInfo requestInfo, Executor appExecutor, MetadataApplier applier) {

                        appExecutor.execute(new Runnable(){
                            @Override
                            public void run() {
                                try {
                                    Metadata headers = new Metadata();
                                    Metadata.Key<String> clientIdKey = Metadata.Key.of("X-Client-Id", Metadata.ASCII_STRING_MARSHALLER);
                                    headers.put(clientIdKey, TEST_CLIENT_ID);
                                    applier.apply(headers);
                                } catch(Throwable ex) {
                                    applier.fail(Status.UNAUTHENTICATED.withCause(ex));
                                }
                            }
                        });


                }

                @Override
                public void thisUsesUnstableApi() { }

            });
    }

    @Test
    public void testGetSipCredentials() throws IOException {
        
        SipCredentialsRequest req = SipCredentialsRequest.newBuilder().build();
        SipCredentialsResponse res = client.getSipCredentials(req);
        
        assertEquals(1, res.getCredentialsCount());
        SipCredentials cred = res.getCredentials(0);
        assertEquals("test", cred.getUsername());
        assertEquals("secret", cred.getPassword());
        assertEquals("sip://at.dev", cred.getHost());
        assertEquals(5060, cred.getPort());
    }

    @Test
    public void testGetToken() throws IOException {
        ClientTokenRequest req = ClientTokenRequest.newBuilder().build();
        ClientTokenResponse res = client.getToken(req);
        assertNotNull(res);
        assertNotNull(res.getToken());
        assertEquals(res.getExpiration() > System.currentTimeMillis(), true);
    }
}