package com.africastalking.test.chat;

import com.africastalking.*;
import com.africastalking.chat.*;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class ChatTest {

    String productId = "test";
    String channelNumber = "AT2FA";

    @Before
    public void setup() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
    }

    @Test
    public void testOptIn() throws IOException {
        ChatService chat = AfricasTalking.getService(AfricasTalking.SERVICE_CHAT);
        ConsentResponse resp = chat.optIn("+254711082302", Channel.WhatApp, channelNumber);
        Assert.assertEquals(true, resp.statusCode == 401);
    }

    @Test
    public void testOptOut() throws IOException {
        ChatService chat = AfricasTalking.getService(AfricasTalking.SERVICE_CHAT);
        ConsentResponse resp = chat.optOut("+254711082302", Channel.WhatApp, channelNumber);
        Assert.assertEquals(true, resp.statusCode == 401);
    }

    @Test
    public void testSendMessage() throws IOException {
        ChatService chat = AfricasTalking.getService(AfricasTalking.SERVICE_CHAT);
        ChatResponse resp = chat.sendMessage(productId, "+254711082302", Channel.Telegram, channelNumber, "hello hello");
        Assert.assertNotNull(resp.messageId);
        Assert.assertNotNull(resp.customerId);

        resp = chat.sendMessage(productId, "+254711082302", Channel.Telegram, channelNumber, 34.55f, 55.6f);
        Assert.assertNotNull(resp.messageId);
        Assert.assertNotNull(resp.customerId);

        resp = chat.sendMessage(productId, "+254711082302", Channel.Telegram, channelNumber, MediaType.Image, "https://africastalking.com/fake/media.png");
        Assert.assertNotNull(resp.messageId);
        Assert.assertNotNull(resp.customerId);
    }

    @Test
    public void testSendTemplate() throws IOException {
        ChatService chat = AfricasTalking.getService(AfricasTalking.SERVICE_CHAT);
        ChatResponse resp = chat.sendTemplate(productId, "+254711082302", Channel.WhatApp, channelNumber, new Template("fake", new ArrayList<>()));
        Assert.assertNotNull(resp.messageId);
        Assert.assertNotNull(resp.customerId);
    }

}
