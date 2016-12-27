package com.africastalking.example;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.africastalking.*;
import com.jraska.console.timber.ConsoleTree;
import timber.log.Timber;

public class MainActivity extends Activity {


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
        final SMSService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);


        final EditText phone = (EditText) findViewById(R.id.phone);
        final EditText message = (EditText) findViewById(R.id.message);
        Button send = (Button) findViewById(R.id.send);

        Timber.plant(new ConsoleTree());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });


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

}

