package com.africastalking;

import com.africastalking.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class RpcServerTest {

    @Before
    public void init() throws IOException {
        RpcServer server = new RpcServer(Fixtures.USERNAME, Fixtures.API_KEY);
        server.start();
    }

}
