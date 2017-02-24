package com.africastalking.example;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.*;
import com.africastalking.*;
import com.jraska.console.timber.ConsoleTree;
import timber.log.Timber;

public class MainActivity extends Activity {

    static SMSService sms;
    static PaymentsService payment;
    static AirtimeService airtime;


    static {
        Timber.plant(new ConsoleTree());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AfricasTalking.initialize(BuildConfig.USERNAME, BuildConfig.API_KEY, Format.JSON);
        AfricasTalking.setEnvironment(Environment.SANDBOX);
        AfricasTalking.enableLogging(false); // Set true to display SDK logs in console
        AfricasTalking.setLogger(new Logger() {
            @Override
            public void log(String s, Object... objects) {
                Timber.v(s);
            }
        });
        sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        payment = AfricasTalking.getService(PaymentsService.class);
        airtime = AfricasTalking.getService(AirtimeService.class);


        Timber.d("Getting account...");
        AccountService accountService = AfricasTalking.getService(AccountService.class);
        accountService.getUser(new Callback<String>() {
            @Override
            public void onSuccess(String s) {
                Timber.i(s);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Timber.e(throwable.getMessage());
            }
        });

        setupPager();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setupPager() {
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new PagerAdapter(this));

        PagerTabStrip strip = (PagerTabStrip) findViewById(R.id.tabStrip);
        strip.setTabIndicatorColorResource(R.color.accent);
    }

}

