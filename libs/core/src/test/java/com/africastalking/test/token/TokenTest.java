package com.africastalking.test.token;

import com.africastalking.AfricasTalking;
import com.africastalking.TokenService;
import com.africastalking.test.Fixtures;
import com.africastalking.token.AuthTokenResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TokenTest {

    @Before
    public void setup() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
    }

    @Test
    public void testGenerateAuthToken() {
        TokenService service = AfricasTalking.getService(TokenService.class);
        try {
            final AuthTokenResponse response = service.generateAuthToken();
            Assert.assertEquals(3600, response.lifetimeInSeconds);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

}
