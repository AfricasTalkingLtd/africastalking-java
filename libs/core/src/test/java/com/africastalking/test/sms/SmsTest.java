package com.africastalking.test.sms;

import com.africastalking.AfricasTalking;
import com.africastalking.Callback;
import com.africastalking.SmsService;
import com.africastalking.Status;
import com.africastalking.sms.Message;
import com.africastalking.sms.Recipient;
import com.africastalking.sms.Subscription;
import com.africastalking.sms.SubscriptionResponse;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SmsTest {

    @Before
    public void setup() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
    }

    @Test
    public void testSendHighMultipleVolume() throws IOException, InterruptedException {
        int count = 100000;
        CountDownLatch lock = new CountDownLatch(count);
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        for (int i = 0; i < count; i++) {
            int offset = count + i;
            String[] numbers = new String[] { "+254711" + offset };
            sms.send("testSendHighMultipleVolume(" + numbers[0] + ")", "AT2FA", numbers, true, new Callback<List<Recipient>>() {
                @Override
                public void onSuccess(List<Recipient> response) {
                    Assert.assertNotNull(response);
                    lock.countDown();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Assert.fail(throwable.getMessage());
                    lock.countDown();
                }
            });
        }
        lock.await(Fixtures.TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testSendHighSingleVolume() throws IOException, InterruptedException {
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);

        int count = 100000;
        CountDownLatch lock = new CountDownLatch(1);

        String[] numbers = new String[count];
        for (int i = 0; i < count; i++) {
            int offset = count + i;
            numbers[i] = "+254711" + offset;
        }
        sms.send("testSendHighSingleVolume()", "AT2FA", numbers, true, new Callback<List<Recipient>>() {
                @Override
                public void onSuccess(List<Recipient> response) {
                    System.out.println(response.get(0).toString());
                    Assert.assertNotNull(response);
                    lock.countDown();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Assert.fail(throwable.getMessage());
                    lock.countDown();
                }
        });

        lock.await(Fixtures.TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testSend() throws IOException {
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        List<Recipient> resp = sms.send("testSend()", "AT2FA", new String[] {"+254711082302", "+254731034588"}, false);
        Assert.assertEquals(2, resp.size());
        Assert.assertEquals(Status.SUCCESS, resp.get(0).status);
        Assert.assertEquals(101, resp.get(0).statusCode);
    }

    @Test
    public void testSendPremium() throws IOException {

        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        List<Recipient> resp = sms.sendPremium("testSendPremium()", "8989", "KiKi", "Linky", 10, new String[] {"+254711082302", "+254731034588"});
        Assert.assertEquals(2, resp.size());
        Assert.assertEquals(Status.SUCCESS, resp.get(0).status);

    }

    @Test
    public void testFetchMessages() throws IOException {
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        List<Message> resp = sms.fetchMessages(0);
        Assert.assertEquals(true, resp.size() >= 0);
    }

    @Test
    public void testFetchSubscriptions() throws IOException {
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        List<Subscription> resp = sms.fetchSubscriptions("AT2FA", "KiKi", 0);
        Assert.assertEquals(true, resp.size() >= 0);
    }

    @Test
    public void testCreateSubscription() throws IOException {
        String phone = "+254731034" + ThreadLocalRandom.current().nextInt(100, 999);
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        
        SubscriptionResponse resp = sms.createSubscription("AT2FA", "KiKi", phone);
        Assert.assertEquals("Waiting for user input", resp.description);
    }

    @Test
    public void testDeleteSubscription() throws IOException {
        String phone = "+254731034" + ThreadLocalRandom.current().nextInt(100, 999);
        SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        SubscriptionResponse resp = sms.deleteSubscription("AT2FA", "KiKi", phone);
        Assert.assertEquals("Success", resp.status);
    }

}
