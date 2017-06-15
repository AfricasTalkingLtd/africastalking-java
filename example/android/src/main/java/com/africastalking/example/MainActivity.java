package com.africastalking.example;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.africastalking.Account;
import com.africastalking.Callback;
import com.africastalking.AfricastalkingClient;
import com.jraska.console.timber.ConsoleTree;
import timber.log.Timber;

public class MainActivity extends Activity {

    static {
        Timber.plant(new ConsoleTree());
    }


    private void initSDK(String token) {
        AfricastalkingClient.initialize(BuildConfig.RPC_HOST, BuildConfig.RPC_PORT, token);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupConnect();
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

    private void setupConnect() {
        final EditText tokenInput = (EditText) findViewById(R.id.token);
        Button connectButton = (Button) findViewById(R.id.connect);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = tokenInput.getEditableText().toString();
                initSDK(token);

                Account account = AfricastalkingClient.getAccountService();

                Timber.d("Getting account...");
                account.getUser(new Callback<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Timber.i(s);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                    }
                });
            }
        });
    }


     private void setupPager() {
         ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
         mViewPager.setAdapter(new PagerAdapter(this));

         PagerTabStrip strip = (PagerTabStrip) findViewById(R.id.tabStrip);
         strip.setTabIndicatorColorResource(R.color.accent);
     }

}

