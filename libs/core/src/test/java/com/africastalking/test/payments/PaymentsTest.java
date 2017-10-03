package com.africastalking.test.payments;

import com.africastalking.*;
import com.africastalking.payments.recipient.Business;
import com.africastalking.payments.recipient.Consumer;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;


public class PaymentsTest {

    @Before
    public void setup() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
        AfricasTalking.setLogger(new Logger() {
            @Override
            public void log(String message, Object... args) {
                System.err.println(message);
            }
        });
    }

    @Test
    public void testCheckout() throws IOException {
        PaymentsService service = AfricasTalking.getService(PaymentsService.class);
        String resp = service.checkout("TestProduct", "0711082302", "KES 877");
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testPayConsumer() throws IOException {
        PaymentsService service = AfricasTalking.getService(PaymentsService.class);
        Consumer recip = new Consumer("Salama", "0711082302", "KES 432");
        String resp = service.payConsumer("TestProduct", recip);
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testPayBusiness() throws IOException {
        PaymentsService service = AfricasTalking.getService(PaymentsService.class);
        Business recip = new Business("SBDev", "AccDest", Business.TransferType.TRANSFER, "KES 24512");
        String resp = service.payBusiness("TestProduct", recip);
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }


}
