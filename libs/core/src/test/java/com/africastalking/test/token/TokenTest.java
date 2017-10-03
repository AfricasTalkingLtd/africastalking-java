package com.africastalking.test.token;

import com.africastalking.*;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TokenTest {


    @Before
    public void setup() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
    }

    @Test
    public void testCreateCheckoutToken() {

        TokenService service = AfricasTalking.getService(TokenService.class);
        try {
            final String response = service.createCheckoutToken("0718769882");
            Assert.assertNotNull(response);

        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

    }

}
