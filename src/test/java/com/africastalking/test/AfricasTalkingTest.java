package com.africastalking.test;

import com.africastalking.AccountService;
import com.africastalking.AfricasTalking;
import com.africastalking.Currency;
import com.africastalking.Format;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AfricasTalkingTest {

    @Test
    public void testInitialization() {

        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY, Format.JSON, Currency.KES, Fixtures.DEBUG);
        AccountService ac1 = AfricasTalking.getService(AccountService.class);
        AccountService ac2 = AfricasTalking.getService(AfricasTalking.SERVICE_ACCOUNT);
        assertEquals(ac1, ac2);
    }

    @Test
    public void testJsonFormat() throws IOException {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY, Format.JSON, Currency.KES, Fixtures.DEBUG);
        AccountService service = AfricasTalking.getService(AfricasTalking.SERVICE_ACCOUNT);
        String json = service.getUser();
        // TODO: Proper json validation
        Assert.assertTrue("Invalid json", json.startsWith("{"));
    }

    @Test
    public void testXMLFormat() throws IOException {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY, Format.XML, Currency.KES, Fixtures.DEBUG);
        AccountService service = AfricasTalking.getService(AfricasTalking.SERVICE_ACCOUNT);
        String xml = service.getUser();
        // TODO: proper xml validation
        Assert.assertTrue("Invalid xml", xml.startsWith("<"));
    }

}
