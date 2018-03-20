package com.africastalking.test;

import com.africastalking.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AfricasTalkingTest {


    @Test
    public void testInitialization() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
        ApplicationService ac1 = AfricasTalking.getService(ApplicationService.class);
        ApplicationService ac2 = AfricasTalking.getService(AfricasTalking.SERVICE_APPLICATION);
        assertEquals(ac1, ac2);
    }
}
