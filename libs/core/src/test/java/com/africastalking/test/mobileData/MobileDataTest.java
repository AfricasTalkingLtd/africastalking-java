package com.africastalking.test.mobileData;

import com.africastalking.AfricasTalking;
import com.africastalking.MobileDataService;
import com.africastalking.mobileData.Transaction;
import com.africastalking.mobileData.recipient.MobileDataRecipient;
import com.africastalking.mobileData.response.MobileDataResponse;
import com.africastalking.mobileData.response.WalletBalanceResponse;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MobileDataTest {

    @Before
    public void setup() {
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
    }

    @Test
    public void testMobileData() throws IOException {
        MobileDataService service = AfricasTalking.getService(AfricasTalking.SERVICE_MOBILE_DATA);
        MobileDataRecipient recip = new MobileDataRecipient( "+254718769882", 2, MobileDataRecipient.DataUnit.GB, MobileDataRecipient.DataValidity.Month);
        List<MobileDataRecipient> recipients = Arrays.asList(recip);

        MobileDataResponse resp = service.send("TestProduct", recipients);
        Assert.assertEquals(1, resp.entries.size());
    }

    @Test
    public void testFindTransaction() throws IOException {
        MobileDataService service = AfricasTalking.getService(MobileDataService.class);
        MobileDataRecipient recip = new MobileDataRecipient( "+254718769882", 15, MobileDataRecipient.DataUnit.MB, MobileDataRecipient.DataValidity.Day);
        List<MobileDataRecipient> recipients = Arrays.asList(recip);

        MobileDataResponse resp = service.send("TestProduct", recipients);
        System.out.println(resp.toString());

        String transactionId = resp.entries.get(0).transactionId;
        Transaction transaction = service.findTransaction(transactionId);
        Assert.assertEquals(transactionId, transaction.transactionId);
    }

    @Test
    public void testWalletBalance() throws IOException {
        MobileDataService service = AfricasTalking.getService(AfricasTalking.SERVICE_MOBILE_DATA);
        WalletBalanceResponse response = service.fetchWalletBalance();
        Assert.assertEquals(true, response.balance.startsWith("KES "));
    }
}
