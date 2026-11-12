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

    @Test
    public void testMobileDataServiceResetOnInitialize(){
        AfricasTalking.initialize("account1","key1");
        MobileDataService m1 = AfricasTalking.getService(MobileDataService.class);

        // Second set credentials
        AfricasTalking.initialize("account2","key2");
        MobileDataService m2 = AfricasTalking.getService(MobileDataService.class);

        // When destroyed and re initialized, a new instance should be created
        org.junit.Assert.assertNotSame(m1,m2);
    }
}
