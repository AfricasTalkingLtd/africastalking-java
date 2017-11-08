package com.africastalking.test.payment;

import com.africastalking.*;
import com.africastalking.payments.CardCheckoutResponse;
import com.africastalking.payments.CheckoutResponse;
import com.africastalking.payments.recipient.Business;
import com.africastalking.payments.recipient.Consumer;
import com.africastalking.payments.PaymentCard;
import com.africastalking.payments.BankAccount;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class PaymentTest {

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
    public void testMobileCheckout() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        CheckoutResponse resp = service.mobileCheckout("TestProduct", "0711082302", "KES 877", new HashMap());
        Assert.assertEquals(CheckoutResponse.STATUS_PENDING, resp.status);
    }

    @Test
    public void testCardCheckout() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        PaymentCard card = new PaymentCard(9223372036854775807L, 232, 10, 23, "NG","1222");
        card.number = 9223372036854775807L;
        CardCheckoutResponse resp = service.cardCheckout("TestProduct", "NGN 877", card, new HashMap());
        Assert.assertEquals(CheckoutResponse.STATUS_PENDING, resp.status);
    }

    @Test
    public void testCardCheckoutValidation() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        String resp = service.validateCardCheckout("sometxId", "someToken");
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testBankCheckout() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        BankAccount account = new BankAccount("salama", "084802842", "Zenith Bank", "NG");
        String resp = service.bankCheckout("TestProduct", "NGN 877", account, new HashMap());
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testBankCheckoutValidation() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        String resp = service.validateBankCheckout("sometxId", "someToken");
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testPayConsumer() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        Consumer recip = new Consumer("Salama", "0711082302", "KES 432", null);
        List<Consumer> consumers = Arrays.asList(recip);
        String resp = service.payConsumers("TestProduct", consumers);
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testPayBusiness() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        Business recip = new Business("SBDev", "AccDest", Business.TRANSFER_TYPE_B2B, Business.PROVIDER_ATHENA, "KES 24512");
        String resp = service.payBusiness("TestProduct", recip);
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }


}
