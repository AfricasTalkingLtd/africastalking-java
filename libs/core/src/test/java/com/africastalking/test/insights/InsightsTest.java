package com.africastalking.test.insights;

import com.africastalking.AfricasTalking;
import com.africastalking.InsightsService;
import com.africastalking.insights.SimSwapResponse;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class InsightsTest {


    @Before
    public void setup() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
    }

    @Test
    public void testCheckSimSwapState() {
        InsightsService service = AfricasTalking.getService(InsightsService.class);
        try {
            ArrayList<String> phones = new ArrayList<>();
            phones.add("+254710000000");
            phones.add("+254770000000");
            final SimSwapResponse response = service.checkSimSwapState(phones);
            Assert.assertEquals("Processed", response.status);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }
}
