package com.africastalking.example;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.africastalking.*;
import com.jraska.console.timber.ConsoleTree;
import timber.log.Timber;

public class MainActivity extends Activity {

    //private EditText phone, message;
    private SMSService sms;
    private PaymentsService payment;


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
        mViewPager.setAdapter(new CustomPagerAdapter(this));

        PagerTabStrip strip = (PagerTabStrip) findViewById(R.id.tabStrip);
        strip.setTabIndicatorColorResource(R.color.accent);
    }



    enum CustomPagerEnum {

        SMS("SMS", R.layout.fragment_sms),
        PAYMENT("Payment", R.layout.fragment_payment);

        private String mTitle;
        private int mLayoutResId;

        CustomPagerEnum(String title, int layoutResId) {
            mTitle = title;
            mLayoutResId = layoutResId;
        }

        public String getTitle() {
            return mTitle;
        }

        public int getLayoutResId() {
            return mLayoutResId;
        }

    }


    public class CustomPagerAdapter extends PagerAdapter {


        private Context mContext;

        public CustomPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup layout = (ViewGroup) inflater.inflate(customPagerEnum.getLayoutResId(), collection, false);
            collection.addView(layout);

            switch (position){
                case 0: // SMS
                    setupSMS(layout);
                    break;
                case 1: // Payments
                    setupPayment(layout);
                    break;
            }



            return layout;
        }

        private void setupSMS(View root) {

            final EditText phone = (EditText) root.findViewById(R.id.phone);
            final EditText message = (EditText) root.findViewById(R.id.message);
            final Button btnSend = (Button) root.findViewById(R.id.btnSend);

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendSMS(phone.getText().toString(), message.getText().toString());
                }
            });

            message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_ACTION_SEND) {
                        sendSMS(phone.getText().toString(), message.getText().toString());
                    }
                    return false;
                }
            });

        }

        private void setupPayment(View root) {

            final Button btnCheckout = (Button) root.findViewById(R.id.btnCheckout);

            btnCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkout();
                }
            });


        }









        private void checkout() {
            payment.checkout("TestProduct", "0718769882", 34234, Currency.KES, null, new Callback<String> (){
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

        private void sendSMS(String number, String text) {
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


        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return CustomPagerEnum.values().length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
            return customPagerEnum.getTitle();
        }

    }



}

