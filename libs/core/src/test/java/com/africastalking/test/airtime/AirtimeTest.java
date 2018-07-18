package com.africastalking.test.airtime;

import com.africastalking.AfricasTalking;
import com.africastalking.AirtimeService;
import com.africastalking.airtime.AirtimeResponse;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;


public class AirtimeTest {

    @Before
    public void setup() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
    }

    @Test
    public void testSendSingle() throws IOException {
        AirtimeService service = AfricasTalking.getService(AirtimeService.class);
        AirtimeResponse response = service.send("+254711082302", "KES " + ThreadLocalRandom.current().nextInt(10, 10001));
        Assert.assertEquals(1, response.numSent);
    }

    @Test
    public void testSendMultiple() throws IOException {
        AirtimeService service = AfricasTalking.getService(AirtimeService.class);
        HashMap<String, String> people = new HashMap<>();
        people.put("+254731034588", "KES " + ThreadLocalRandom.current().nextInt(500, 5501));
        people.put("+254711082302", "UGX " + ThreadLocalRandom.current().nextInt(3000, 8001));
        AirtimeResponse response = service.send(people);
        Assert.assertEquals(2, response.numSent);
    }

}
