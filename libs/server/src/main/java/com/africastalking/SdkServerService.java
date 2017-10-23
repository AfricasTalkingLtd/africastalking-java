package com.africastalking;

import java.util.ArrayList;
import java.util.List;
import com.africastalking.AfricasTalking;
import com.africastalking.proto.SdkServerServiceGrpc.*;
import com.africastalking.proto.SdkServerServiceOuterClass;
import com.africastalking.proto.SdkServerServiceOuterClass.*;
import com.google.gson.Gson;

import io.grpc.stub.StreamObserver;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

final class SdkServerService extends SdkServerServiceImplBase {

    private ArrayList<SipCredentials> credentialsList = new ArrayList<>();

    private Gson gson = new Gson();

    SdkServerService() {}

    void addSipCredentials(String username, String password, String host, int port, String transport) {
        credentialsList.add(new SipCredentials(username, password, host, port, transport));
    }

    @Override
    public void getToken(ClientTokenRequest request, final StreamObserver<ClientTokenResponse> response) {

        try {
            TokenService service = AfricasTalking.getService(AfricasTalking.SERVICE_TOKEN);
            String tokenJson = service.generateAuthToken();
            TokenResponse tk = gson.fromJson(tokenJson, TokenResponse.class);
            ClientTokenResponse tokenResponse = ClientTokenResponse.newBuilder()
                    .setToken(tk.token)
                    .setExpiration(System.currentTimeMillis() + (tk.lifetimeInSeconds * 1000))
                    .setUsername(AfricasTalking.sUsername)
                    .setEnvironment(Service.isSandbox ? "sandbox" : "production")
                    .build();
            response.onNext(tokenResponse);
            response.onCompleted();

        } catch (Exception ex) {
            response.onError(ex);
        }
    }

    @Override
    public void getSipCredentials(SipCredentialsRequest request, StreamObserver<SipCredentialsResponse> response) {
        List<SdkServerServiceOuterClass.SipCredentials> credentials = new ArrayList<>();
        for(SipCredentials item: credentialsList) {
            SdkServerServiceOuterClass.SipCredentials cred = SdkServerServiceOuterClass.SipCredentials.newBuilder()
                .setHost(item.host)
                .setPort(item.port)
                .setUsername(item.username)
                .setPassword(item.password)
                .setTransport(item.transport)
                .build();
            credentials.add(cred);
        }
        SipCredentialsResponse res = SipCredentialsResponse.newBuilder().addAllCredentials(credentials).build();
        response.onNext(res);
        response.onCompleted();
    }



    class SipCredentials {
        String username, password;
        String host;
        int port;
        String transport;

        SipCredentials(String username, String password, String host, int port, String transport) {
            this.username = username;
            this.password = password;
            this.host = host;
            this.port = port;
            this.transport = transport;
        }
    }

    class TokenRequest {
        String username;
        TokenRequest(String username) {
            this.username = username;
        }
    }

    class TokenResponse {
        String token;
        long lifetimeInSeconds;
    }
}