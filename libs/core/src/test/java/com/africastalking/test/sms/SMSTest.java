package com.africastalking.test.sms;

import com.africastalking.AfricasTalking;
import com.africastalking.Environment;
import com.africastalking.Format;
import com.africastalking.SMSService;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SMSTest {

    static {
        AfricasTalking.setEnvironment(Environment.SANDBOX);
    }

    @Before
    public void setup() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY, Format.JSON, Fixtures.CURRENCY);
    }

    @Test
    public void testSend() throws IOException {
        SMSService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        String resp = sms.send("testSend()", "AT2FA", new String[] {"0711082302", "0731034588"});
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testSendBulk() throws IOException {
        SMSService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        String resp = sms.sendBulk("testSendBulk()", "AT2FA", true, new String[] {"0711082302", "0731034588"});
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testSendPremium() throws IOException {

        SMSService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        String resp = sms.sendPremium("testSendPremium()", "AT2FA", "KiKi", "Linky", 10, new String[] {"0711082302", "0731034588"});
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);

    }

    @Test
    public void testFetchMessage() throws IOException {
        SMSService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        String resp = sms.fetchMessage("0");
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testFetchSubscription() throws IOException {
        SMSService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        String resp = sms.fetchSubscription("AT2FA", "KiKi", "0");
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testCreateSubscription() throws IOException {
        SMSService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        String resp = sms.createSubscription("AT2FA", "KiKi", "0731034588");
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

}
