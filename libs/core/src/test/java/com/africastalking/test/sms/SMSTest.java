package com.africastalking.test.sms;

import com.africastalking.AfricasTalking;
import com.africastalking.SmsService;
import com.africastalking.TokenService;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Type;
import java.util.HashMap;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

public class SMSTest {

    @Before
    public void setup() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
    }

    @Test
    public void testSend() throws IOException {
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        String resp = sms.send("testSend()", "AT2FA", new String[] {"0711082302", "0731034588"});
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testSendBulk() throws IOException {
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        String resp = sms.sendBulk("testSendBulk()", "AT2FA", true, new String[] {"0711082302", "0731034588"});
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testSendPremium() throws IOException {

        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        String resp = sms.sendPremium("testSendPremium()", "8989", "KiKi", "Linky", 10, new String[] {"0711082302", "0731034588"});
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);

    }

    @Test
    public void testFetchMessage() throws IOException {
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        String resp = sms.fetchMessage("0");
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testFetchSubscription() throws IOException {
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        String resp = sms.fetchSubscription("AT2FA", "KiKi", "0");
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testCreateSubscription() throws IOException {
        String phone = "0731034588";
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        TokenService tokenService = AfricasTalking.getService(AfricasTalking.SERVICE_TOKEN);
        Type type = new TypeToken<HashMap<String,String>>(){}.getType();
        HashMap<String,String> checkoutToken = new Gson().fromJson(tokenService.createCheckoutToken(phone), type);
        
        String resp = sms.createSubscription("AT2FA", "KiKi", phone, checkoutToken.get("token"));
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

}
