package com.africastalking.example;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.africastalking.*;
import com.jraska.console.timber.ConsoleTree;
import timber.log.Timber;

public class MainActivity extends Activity {

    static {
        Timber.plant(new ConsoleTree());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RpcClient.initialize(BuildConfig.RPC_HOST, BuildConfig.RPC_PORT);
        Account accountService = RpcClient.getAccountService();

        Timber.d("Getting account...");
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

        // setupPager();

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


    // private void setupPager() {
    //     ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
    //     mViewPager.setAdapter(new PagerAdapter(this));

    //     PagerTabStrip strip = (PagerTabStrip) findViewById(R.id.tabStrip);
    //     strip.setTabIndicatorColorResource(R.color.accent);
    // }

}

