package com.africastalking.test.sms;

import com.africastalking.AfricasTalking;
import com.africastalking.SmsService;
import com.africastalking.Status;
import com.africastalking.TokenService;
import com.africastalking.sms.Message;
import com.africastalking.sms.Recipient;
import com.africastalking.sms.Subscription;
import com.africastalking.sms.SubscriptionResponse;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.africastalking.token.CheckoutTokenResponse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SmsTest {

    @Before
    public void setup() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
    }

    @Test
    public void testSend() throws IOException {
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        List<Recipient> resp = sms.send("testSend()", "AT2FA", new String[] {"0711082302", "0731034588"}, false);
        Assert.assertEquals(2, resp.size());
        Assert.assertEquals(Status.SUCCESS, resp.get(0).status);
    }

    @Test
    public void testSendPremium() throws IOException {

        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        List<Recipient> resp = sms.sendPremium("testSendPremium()", "8989", "KiKi", "Linky", 10, new String[] {"0711082302", "0731034588"});
        Assert.assertEquals(2, resp.size());
        Assert.assertEquals(Status.SUCCESS, resp.get(0).status);

    }

    @Test
    public void testFetchMessages() throws IOException {
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        List<Message> resp = sms.fetchMessages("0");
        Assert.assertEquals(true, resp.size() >= 0);
    }

    @Test
    public void testFetchSubscriptions() throws IOException {
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        List<Subscription> resp = sms.fetchSubscriptions("AT2FA", "KiKi", "0");
        Assert.assertEquals(true, resp.size() >= 0);
    }

    @Test
    public void testCreateSubscription() throws IOException {
        String phone = "0731034" + ThreadLocalRandom.current().nextInt(100, 999);
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        TokenService tokenService = AfricasTalking.getService(AfricasTalking.SERVICE_TOKEN);
        CheckoutTokenResponse checkoutToken = tokenService.createCheckoutToken(phone);
        
        SubscriptionResponse resp = sms.createSubscription("AT2FA", "KiKi", phone, checkoutToken.token);
        Assert.assertEquals("Waiting for user input", resp.description);
    }

}
