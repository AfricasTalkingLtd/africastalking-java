package com.africastalking.test.voice;

import com.africastalking.*;
import com.africastalking.test.Fixtures;
import com.africastalking.voice.CallResponse;
import com.africastalking.voice.QueuedCallsResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class VoiceTest {


    @Before
    public void setup() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
    }

    @Test
    public void testCall() {
        VoiceService service = AfricasTalking.getService(VoiceService.class);
        try {
            final CallResponse response = service.call("0718769882", "0718769881");
            Assert.assertEquals("Invalid callerId: 0718769881", response.errorMessage);

        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testFetchQueuedCalls() {
        VoiceService service = AfricasTalking.getService(VoiceService.class);
        try {
            final QueuedCallsResponse response = service.fetchQueuedCalls("0718769882");
            Assert.assertEquals(0, response.numCalls);

        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testUploadMediaFile() {
        VoiceService service = AfricasTalking.getService(VoiceService.class);
        try {
            final String response = service.uploadMediaFile("+254718769882", "http://defef.klo/wave.mp3");
            Assert.assertNotNull(response);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

}
