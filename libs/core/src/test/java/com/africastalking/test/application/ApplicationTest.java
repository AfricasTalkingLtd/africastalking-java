package com.africastalking.test.application;

import com.africastalking.*;
import com.africastalking.application.ApplicationResponse;
import com.africastalking.test.Fixtures;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class ApplicationTest {

    CountDownLatch lock;

    @Before
    public void setup() {
        lock = new CountDownLatch(10);
        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY);
    }

    @Test
    public void testFetchApplicationData() {

        ApplicationService service = AfricasTalking.getService(ApplicationService.class);
        try {
            final ApplicationResponse resp = service.fetchApplicationData();
            Assert.assertNotNull(resp);

            service.fetchApplicationData(new Callback<ApplicationResponse>() {
                @Override
                public void onSuccess(ApplicationResponse response) {
                    Assert.assertNotNull(response);
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
