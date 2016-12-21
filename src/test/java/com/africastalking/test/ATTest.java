package com.africastalking.test;

import com.africastalking.AccountService;
import com.africastalking.AfricasTalking;
import com.africastalking.Format;
import org.junit.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ATTest {

    @Test
    public void testInitialization() {

        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY, Format.JSON, Fixtures.DEBUG);
        AccountService ac1 = AfricasTalking.getService(AccountService.class);
        AccountService ac2 = AfricasTalking.getService(AfricasTalking.SERVICE_ACCOUNT);
        assertEquals(ac1, ac2);

        try {
            Response<String> resp = ac1.fetchUser().execute();
            System.err.print(resp.body());
        } catch (IOException e) {
            e.printStackTrace();
        }


        assertEquals(4,6);
    }

}
