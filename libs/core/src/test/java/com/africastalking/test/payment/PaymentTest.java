package com.africastalking.test.payment;

import com.africastalking.*;
import com.africastalking.Status;
import com.africastalking.payment.recipient.MobileDataRecipient;
import com.africastalking.test.Fixtures;
import com.africastalking.payment.response.*;
import com.africastalking.payment.PaymentCard;
import com.africastalking.payment.Transaction;
import com.africastalking.payment.BankAccount;
import com.africastalking.payment.recipient.Bank;
import com.africastalking.payment.recipient.Business;
import com.africastalking.payment.recipient.Consumer;
import com.africastalking.payment.WalletTransaction;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class PaymentTest {

    @Before
    public void setup() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
        // AfricasTalking.setLogger(new Logger() {
        //     @Override
        //     public void log(String message, Object... args) {
        //         System.out.println(message);
        //     }
        // });
    }

    @Test
    public void testMobileCheckout() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        CheckoutResponse resp = service.mobileCheckout("TestProduct", "+254711082302", "KES", 577, new HashMap(), "1234");
        Assert.assertEquals(Status.PENDING_CONFIRMATION, resp.status);
    }

    @Test
    public void testCardCheckout() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        PaymentCard card = new PaymentCard("4223372036854775807", 1232, 10, 2022, "NG", "0022");
        CheckoutResponse resp = service.cardCheckoutCharge("TestProduct", "KES", ThreadLocalRandom.current().nextInt(200, 15001), card, "Test card checkout?", new HashMap());
        Assert.assertEquals(Status.PENDING_VALIDATION, resp.status);
    }

    @Test
    public void testCardCheckoutValidation() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        CheckoutValidateResponse resp = service.cardCheckoutValidate("sometxId_" + System.currentTimeMillis(), "someToken_" + System.currentTimeMillis());
        Assert.assertEquals(Status.INVALID_REQUEST, resp.status);
    }

    @Test
    public void testBankCheckout() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        BankAccount account = new BankAccount("salama", "084802842", BankAccount.BankCode.Zenith_NG);
        CheckoutResponse resp = service.bankCheckoutCharge("Ikoyi Store", "NGN", ThreadLocalRandom.current().nextInt(700, 15001), account,"Some narration", new HashMap());
        Assert.assertEquals(Status.INVALID_REQUEST, resp.status);
    }

    @Test
    public void testBankCheckoutValidation() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        CheckoutValidateResponse resp = service.bankCheckoutValidate("sometxId_" + System.currentTimeMillis(), "someToken_" + System.currentTimeMillis());
        Assert.assertEquals(Status.INVALID_REQUEST, resp.status);
    }

    @Test
    public void testBankTransfer() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        List<Bank> recipients = Arrays.asList(new Bank(new BankAccount("Bob", "2323", BankAccount.BankCode.Zenith_NG), "NGN", ThreadLocalRandom.current().nextInt(700, 15001), "Some narration", new HashMap<String, String>()));
        BankTransferResponse resp = service.bankTransfer("Ikoyi Store", recipients);
        Assert.assertEquals(Status.INVALID_REQUEST, resp.entries.get(0).status);
    }

    @Test
    public void testWalletTransfer() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        WalletTransferResponse resp = service.walletTransfer("Ikoyi Store", 2211, "KES", ThreadLocalRandom.current().nextInt(700, 15001), new HashMap<>());
        Assert.assertEquals(Status.FAILED, resp.status);
    }

    @Test
    public void testTopupStash() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        TopupStashResponse resp = service.topupStash("Ikoyi Store", "KES", ThreadLocalRandom.current().nextInt(700, 15001), new HashMap<>());
        Assert.assertEquals(Status.FAILED, resp.status);
    }

    @Test
    public void testMobileB2C() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        Consumer recip = new Consumer("Salama", "+254711082302", "KES", 432, null);
        List<Consumer> consumers = Arrays.asList(recip);
        B2CResponse resp = service.mobileB2C("TestProduct", consumers);
        Assert.assertEquals("KES 432.0000", resp.totalValue);
    }

    @Test
    public void testMobileData() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        MobileDataRecipient recip = new MobileDataRecipient( "+254718769882", 100, MobileDataRecipient.DataUnit.MB, MobileDataRecipient.DataValidity.MONTH);
        List<MobileDataRecipient> recipients = Arrays.asList(recip);

        MobileDataResponse resp = service.mobileData("TestProduct", recipients);
        Assert.assertEquals(1, resp.entries.size());
    }

    @Test
    public void testMobileB2B() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        Business recip = new Business("SBDev", "AccDest", "+254718769882", Business.TRANSFER_TYPE_B2B, Business.PROVIDER_ATHENA, "KES", 2512);
        B2BResponse resp = service.mobileB2B("TestProduct", recip);
        Assert.assertEquals("Queued", resp.status);
    }

    @Test
    public void testFetchTransactions() throws IOException, InterruptedException {
        CountDownLatch lock = new CountDownLatch(10);
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        HashMap<String, String> filters = new HashMap<String, String>();

        service.fetchProductTransactions("TestProduct", filters, new Callback<List<Transaction>>() {
            @Override
            public void onSuccess(List<Transaction> transactions) {
                Assert.assertEquals(true, transactions.size() > 0);
                lock.countDown();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Assert.fail(throwable.getMessage());
                lock.countDown();
            }
        });

        lock.await(Fixtures.TIMEOUT, TimeUnit.MILLISECONDS);

        List<Transaction> transactions = service.fetchProductTransactions("TestProduct", filters);
        Assert.assertEquals(true, transactions.size() > 0);
    }

    @Test
    public void testFindTransaction() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        Business recip = new Business("TestProduct", "AccDest", null, Business.TRANSFER_TYPE_B2B, Business.PROVIDER_ATHENA, "KES", ThreadLocalRandom.current().nextInt(1000, 15001));
        B2BResponse resp = service.mobileB2B("TestProduct", recip);
        Transaction transaction = service.findTransaction(resp.transactionId);
        Assert.assertEquals(resp.transactionId, transaction.transactionId);
    }

    @Test
    public void testFetchWalletTransactions() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        HashMap<String, String> filters = new HashMap<String, String>();
        List<WalletTransaction> transactions = service.fetchWalletTransactions(filters);
        Assert.assertEquals(true, transactions.size() > 0);
    }

    @Test
    public void testWalletBalance() throws IOException {
        PaymentService service = AfricasTalking.getService(PaymentService.class);
        WalletBalanceResponse response = service.fetchWalletBalance();
        Assert.assertEquals(true, response.balance.startsWith("KES "));
    }

}
