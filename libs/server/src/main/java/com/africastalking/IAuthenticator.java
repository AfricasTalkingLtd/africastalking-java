package com.africastalking;

import com.africastalking.proto.Base.*;
import io.grpc.stub.StreamObserver;

interface IAuthenticator {
    boolean isValidToken(Token token, StreamObserver<Response> responseObserver);
}
