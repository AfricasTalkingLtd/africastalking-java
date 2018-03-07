package com.africastalking.test.account;

import com.africastalking.*;
import com.africastalking.account.AccountResponse;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class AccountTest {

    CountDownLatch lock;

    @Before
    public void setup() {
        lock = new CountDownLatch(10);
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
    }

    @Test
    public void testFetchAccount() {

        AccountService service = AfricasTalking.getService(AccountService.class);
        try {
            final AccountResponse resp = service.fetchAccount();
            Assert.assertNotNull(resp);

            service.fetchAccount(new Callback<AccountResponse>() {
                @Override
                public void onSuccess(AccountResponse response) {
                    Assert.assertNotNull(response);
                    assertEquals(resp.userData.balance, response.userData.balance);
                    lock.countDown();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Assert.fail(throwable.getMessage());
                    lock.countDown();
                }
            });

            lock.await(Fixtures.TIMEOUT, TimeUnit.MILLISECONDS);

        } catch (IOException | InterruptedException e) {
            Assert.fail(e.getMessage());
        }

    }

}
