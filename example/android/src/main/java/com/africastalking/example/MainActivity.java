package com.africastalking.example;

import android.app.Activity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.africastalking.Account;
import com.africastalking.Callback;
import com.africastalking.ATClient;
import com.jraska.console.timber.ConsoleTree;
import timber.log.Timber;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MainActivity extends Activity {

    Button connectButton;
    // Button disconnectButton;

    static {
        Timber.plant(new ConsoleTree.Builder().build());
    }


    private void initSDK(String host, int port, String token) {
        ATClient.initialize(host, port, token);
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
        final EditText serverInput = (EditText) findViewById(R.id.server);
        connectButton = (Button) findViewById(R.id.connect);
        // disconnectButton = (Button) findViewById(R.id.disconnect);

        serverInput.setText("http://" + BuildConfig.WEB_HOST);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String server = serverInput.getEditableText().toString();
                (new LoginTask()).execute(new String[] { server });
            }
        });

        /*disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                (new LogoutTask()).execute();
            }
        });*/
    }


     private void setupPager() {
         ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
         mViewPager.setAdapter(new PagerAdapter(this));

         PagerTabStrip strip = (PagerTabStrip) findViewById(R.id.tabStrip);
         strip.setTabIndicatorColorResource(R.color.accent);
     }

     public static class SampleAuthResponse {
         public String token, host;
         public int port;
     }


     class LoginTask extends AsyncTask<String, Void,  SampleAuthResponse> {

        protected SampleAuthResponse doInBackground(String... servers) {
            try {
                String url = servers[0] + "/auth/login";

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                OkHttpClient client = new OkHttpClient();

                RequestBody body = RequestBody.create(JSON, "{ \"username\": \"aaa\", password: \"pwd\" }");
                Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
                Response response = client.newCall(request).execute();
                String json = response.body().string();
                Log.e("JJ", json);
                //
                return new Gson().fromJson(json, SampleAuthResponse.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(SampleAuthResponse result) {
            if (result == null) {
                Timber.e("Failed to connect!");
                return;
            }

            Timber.d("Got token, initializing SDK...");

            initSDK(result.host, result.port, result.token);

            Account account = ATClient.getAccountService();

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


            connectButton.setEnabled(false);
            //disconnectButton.setEnabled(true);
        }
     }

     class LogoutTask extends AsyncTask<Void, Void, Void> {
         @Override
         protected Void doInBackground(Void... voids) {
             // TODO: Logout to revoke token
             return null;
         }

         @Override
         protected void onPostExecute(Void aVoid) {
             super.onPostExecute(aVoid);

             connectButton.setEnabled(true);
             //disconnectButton.setEnabled(false);
         }
     }

}

