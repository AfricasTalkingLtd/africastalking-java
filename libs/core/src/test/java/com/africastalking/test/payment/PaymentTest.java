package com.africastalking.test.payment;

import com.africastalking.*;
import com.africastalking.payments.recipient.Business;
import com.africastalking.payments.recipient.Consumer;
import com.africastalking.payments.PaymentCard;
import com.africastalking.payments.BankAccount;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


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
        String resp = service.mobileCheckout("TestProduct", "0711082302", "KES 877", new HashMap());
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testCardCheckout() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        PaymentCard card = new PaymentCard();
        card.number = 9223372036854775807L;
        String resp = service.cardCheckout("TestProduct", "NGN 877", card, new HashMap());
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
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
        BankAccount account = new BankAccount();
        account.accountName = "salama";
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
        Consumer recip = new Consumer("Salama", "0711082302", "KES 432");
        List<Consumer> consumers = new ArrayList();
        consumers.add(recip);
        String resp = service.payConsumers("TestProduct", consumers);
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }

    @Test
    public void testPayBusiness() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        Business recip = new Business("SBDev", "AccDest", Business.TransferType.BusinessToBusinessTransfer, "KES 24512");
        String resp = service.payBusiness("TestProduct", recip);
        System.out.print("\n" + resp + "\n");
        Assert.assertNotNull(resp);
    }


}
