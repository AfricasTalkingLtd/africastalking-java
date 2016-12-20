package com.africastalking.test;

import com.africastalking.AccountService;
import com.africastalking.AfricasTalking;
import com.africastalking.Format;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ATTest {

    @Test
    public void testInitialization() {

        AfricasTalking.initialize(Fixtures.USERNAME, Fixtures.API_KEY, Format.JSON, Fixtures.DEBUG);
        AccountService account = AfricasTalking.getAccountService();
        try {
            Response<String> resp = account.fetchUser().execute();
            System.err.print(resp.raw().body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }


        assertEquals(4,6);
    }

}
