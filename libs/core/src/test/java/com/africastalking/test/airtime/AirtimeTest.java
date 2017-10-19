package com.africastalking.test.airtime;

import com.africastalking.AfricasTalking;
import com.africastalking.AirtimeService;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;


public class AirtimeTest {

    @Before
    public void setup() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
    }

    @Test
    public void testSendSingle() throws IOException {

        AirtimeService service = AfricasTalking.getService(AirtimeService.class);
        String response = service.send("0711082302", "KES 130.5");
        System.out.println(response);
        Assert.assertNotNull(response);

    }

    @Test
    public void testSendMultiple() throws IOException {
        AirtimeService service = AfricasTalking.getService(AirtimeService.class);
        HashMap<String, String> people = new HashMap<>();
        people.put("0731034588", "KES 54.7");
        people.put("254711082302", "UGX 434.5");
        String response = service.send(people);
        System.out.println(response);
        Assert.assertNotNull(response);
    }

}
