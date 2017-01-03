package com.africastalking.example;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.africastalking.*;
import com.jraska.console.timber.ConsoleTree;
import timber.log.Timber;

public class MainActivity extends Activity {

    private EditText phone, message;
    private SMSService sms;

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


        phone = (EditText) findViewById(R.id.phone);
        message = (EditText) findViewById(R.id.message);

        message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_SEND) {
                    sendSMS();
                }
                return false;
            }
        });

        Timber.plant(new ConsoleTree());
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
            case R.id.mnuSend:
                //
                sendSMS();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendSMS() {
        String number = phone.getText().toString();
        String text = message.getText().toString();
        if (TextUtils.isEmpty(number)) {
            Timber.e("Enter a phone number");
            return;
        }

        if (TextUtils.isEmpty(text)) {
            Timber.e("Enter a message");
            return;
        }

        Timber.d("Sending SMS...");
        sms.send(text, new String[]{number}, new Callback<String>() {
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

}

