package com.africastalking.test;

import com.africastalking.AccountService;
import com.africastalking.AfricasTalking;
import com.africastalking.Callback;
import com.africastalking.Format;
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
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY, Format.JSON, Fixtures.DEBUG);
    }

    @Test
    public void testFetchUser() {

        AccountService service = AfricasTalking.getService(AccountService.class);
        try {
            final String user = service.fetchUser();
            Assert.assertNotNull(user);

            service.fetchUser(new Callback<String>() {
                @Override
                public void onSuccess(String data) {
                    assertEquals(user, data);
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
