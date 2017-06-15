 package com.africastalking.example;

 import android.content.Context;
 import android.text.TextUtils;
 import android.view.KeyEvent;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.view.inputmethod.EditorInfo;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.TextView;
 import timber.log.Timber;
 import com.africastalking.*;

 public class PagerAdapter extends android.support.v4.view.PagerAdapter {

     enum PagesEnum {

         // AIRTIME("Airtime", R.layout.fragment_airtime),
         // SMS("SMS", R.layout.fragment_sms),
         PAYMENT("Payment", R.layout.fragment_payment);

         private String mTitle;
         private int mLayoutResId;

         PagesEnum(String title, int layoutResId) {
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

     private Context mContext;

     public PagerAdapter(Context context) {
         mContext = context;
     }

     @Override
     public Object instantiateItem(ViewGroup collection, int position) {
         PagesEnum customPagerEnum = PagesEnum.values()[position];
         LayoutInflater inflater = LayoutInflater.from(mContext);
         ViewGroup layout = (ViewGroup) inflater.inflate(customPagerEnum.getLayoutResId(), collection, false);
         collection.addView(layout);

         switch (position){
//             case 0: // Airtime
//                 setupAirtime(layout);
//                 break;
//             case 1: // SMS
//                 setupSMS(layout);
//                 break;
             case 0: // Payments
                 setupPayment(layout);
                 break;
         }



         return layout;
     }

//     private void setupAirtime(View root) {
//
//         final EditText phone = (EditText) root.findViewById(R.id.phone);
//         final EditText amount = (EditText) root.findViewById(R.id.amount);
//
//
//         amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//             @Override
//             public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                 if (id == EditorInfo.IME_ACTION_SEND) {
//                     sendAirtime(phone.getText().toString(), Float.valueOf(amount.getText().toString()));
//                 }
//                 return false;
//             }
//         });
//
//         final Button btnSend = (Button) root.findViewById(R.id.btnSend);
//         btnSend.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 sendAirtime(phone.getText().toString(), Float.valueOf(amount.getText().toString()));
//             }
//         });
//
//     }

//     private void setupSMS(View root) {
//
//         final EditText phone = (EditText) root.findViewById(R.id.phone);
//         final EditText message = (EditText) root.findViewById(R.id.message);
//         final Button btnSend = (Button) root.findViewById(R.id.btnSend);
//
//         btnSend.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 sendSMS(phone.getText().toString(), message.getText().toString());
//             }
//         });
//
//         message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//             @Override
//             public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                 if (id == EditorInfo.IME_ACTION_SEND) {
//                     sendSMS(phone.getText().toString(), message.getText().toString());
//                 }
//                 return false;
//             }
//         });
//
//     }

     private void setupPayment(View root) {


         final EditText phone = (EditText) root.findViewById(R.id.phone);
         final EditText amount = (EditText) root.findViewById(R.id.amount);


         amount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
             @Override
             public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                 if (id == EditorInfo.IME_ACTION_SEND) {
                     checkout(phone.getText().toString(), Float.valueOf(amount.getText().toString()));
                 }
                 return false;
             }
         });

         final Button btnCheckout = (Button) root.findViewById(R.id.btnCheckout);
         btnCheckout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 checkout(phone.getText().toString(), Float.valueOf(amount.getText().toString()));
             }
         });
     }


//     private void sendAirtime(String phone, float amount) {
//
//         if (TextUtils.isEmpty(phone)) {
//             Timber.e("Enter a phone number");
//             return;
//         }
//
//         if (amount <= 0) {
//             Timber.e("Enter a valid amount");
//             return;
//         }
//
//         MainActivity.airtime.send(phone, amount, new Callback<String> (){
//             @Override
//             public void onSuccess(String s) {
//                 Timber.i(s);
//             }
//
//             @Override
//             public void onFailure(Throwable throwable) {
//                 Timber.e(throwable.getMessage());
//             }
//         });
//
//     }

     private void checkout(String phone, float amount) {

         if (TextUtils.isEmpty(phone)) {
             Timber.e("Enter a phone number");
             return;
         }

         if (amount <= 0) {
             Timber.e("Enter a valid amount");
             return;
         }

         Payment payment = AfricastalkingClient.getPaymentService();

         payment.checkout(BuildConfig.PAYMENT_PRODUCT, phone, amount, Currency.KES, null, new Callback<String> (){
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

//     private void sendSMS(String number, String text) {
//         if (TextUtils.isEmpty(number)) {
//             Timber.e("Enter a phone number");
//             return;
//         }
//
//         if (TextUtils.isEmpty(text)) {
//             Timber.e("Enter a message");
//             return;
//         }
//
//         Timber.d("Sending SMS...");
//         MainActivity.sms.send(text, new String[]{number}, new Callback<String>() {
//             @Override
//             public void onSuccess(String s) {
//                 Timber.i(s);
//             }
//
//             @Override
//             public void onFailure(Throwable throwable) {
//                 Timber.e(throwable.getMessage());
//             }
//         });
//     }


     @Override
     public void destroyItem(ViewGroup collection, int position, Object view) {
         collection.removeView((View) view);
     }

     @Override
     public int getCount() {
         return PagesEnum.values().length;
     }

     @Override
     public boolean isViewFromObject(View view, Object object) {
         return view == object;
     }

     @Override
     public CharSequence getPageTitle(int position) {
         PagesEnum customPagerEnum = PagesEnum.values()[position];
         return customPagerEnum.getTitle();
     }

 }