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
    public void testSendMessage() throws IOException {
        ChatService chat = AfricasTalking.getService(AfricasTalking.SERVICE_CHAT);
        ChatResponse resp = chat.sendMessage("+254711082302", channelNumber, "hello hello");
        Assert.assertNotNull(resp.messageId);

        resp = chat.sendMessage("+254711082302", channelNumber, MediaMessageBody.MediaType.Image, "https://africastalking.com/fake/media.png");
        Assert.assertNotNull(resp.messageId);
    }

    @Test
    public void testSendTemplate() throws IOException {
        ChatService chat = AfricasTalking.getService(AfricasTalking.SERVICE_CHAT);
        ChatResponse resp = chat.sendTemplate("+254711082302", channelNumber, "fake)d", "Test", new ArrayList<>());
        Assert.assertNotNull(resp.messageId);
    }

}
