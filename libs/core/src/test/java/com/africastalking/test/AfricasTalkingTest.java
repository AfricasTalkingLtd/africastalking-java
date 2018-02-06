package com.africastalking.test;

import com.africastalking.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AfricasTalkingTest {


    @Test
    public void testInitialization() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
        AccountService ac1 = AfricasTalking.getService(AccountService.class);
        AccountService ac2 = AfricasTalking.getService(AfricasTalking.SERVICE_ACCOUNT);
        assertEquals(ac1, ac2);
    }
}
