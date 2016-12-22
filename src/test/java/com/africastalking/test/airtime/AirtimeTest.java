package com.africastalking.test.airtime;

import com.africastalking.AfricasTalking;
import com.africastalking.AirtimeService;
import com.africastalking.Format;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;


public class AirtimeTest {

    @Before
    public void setup() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY, Format.JSON, Fixtures.CURRENCY, Fixtures.DEBUG);
    }

    @Test
    public void testSendSingle() throws IOException {

        AirtimeService service = AfricasTalking.getService(AirtimeService.class);
        String response = service.send("0718769882", 130.5f);
        System.out.println(response);
        Assert.assertNotNull(response);

    }

    @Test
    public void testSendMultiple() throws IOException {
        AirtimeService service = AfricasTalking.getService(AirtimeService.class);
        HashMap<String, Float> people = new HashMap<>();
        people.put("0731034588", 54.7f);
        people.put("254718769882", 434.5f);
        String response = service.send(people);
        System.out.println(response);
        Assert.assertNotNull(response);
    }

}
