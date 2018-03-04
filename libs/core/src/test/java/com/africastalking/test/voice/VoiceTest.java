package com.africastalking.test.voice;

import com.africastalking.*;
import com.africastalking.test.Fixtures;
import com.africastalking.voice.CallResponse;
import com.africastalking.voice.QueuedCallsResponse;

import com.africastalking.voice.action.GetDigits;
import com.africastalking.voice.action.Record;
import com.africastalking.voice.action.Say;
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
            final CallResponse response = service.call("+254718769882", "0718769881");
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
            final String response = service.uploadMediaFile("+254718769889", "http://defef.klo/wave.mp3");
            Assert.assertNotNull(response);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testXmlBuilder() {
        ActionBuilder builder = new ActionBuilder();

        String say = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response><Say voice=\"man\">Your balance is 1234 Shillings</Say></Response>";
        Assert.assertEquals(say, builder.say(new Say("Your balance is 1234 Shillings", false, Say.Voice.MAN)).build());

        builder = new ActionBuilder();

        String getDigits  = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<Response>" +
                "<GetDigits finishOnKey=\"#\">" +
                "<Say>Please enter your account number followed by the hash sign</Say>" +
                "</GetDigits>" +
                "<Record/>" +
                "</Response>";
        String getDigitsTest = builder
                .getDigits(new GetDigits(
                    new Say("Please enter your account number followed by the hash sign"),
                    0,
                    "#",
                    null))
                .record(new Record())
                .build();
        Assert.assertEquals(getDigits, getDigitsTest);

        builder = new ActionBuilder();
        String record  = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<Response>" +
                "<Record playBeep=\"true\" trimSilence=\"true\" maxLength=\"10\" finishOnKey=\"*\">" +
                "<Say>Please say your name after the beep.</Say>" +
                "</Record>" +
                "</Response>";
        String recordTest = builder
                .record(
                        new Record(
                                new Say("Please say your name after the beep."),
                                "*",
                                10,
                                0,
                                true,
                                true,
                                null
                        )
                )
                .build();
        Assert.assertEquals(record, recordTest);

    }

}
