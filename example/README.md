# Simple Use Cases


**Typical Java application**

```java

import static spark.Spark.*;
import com.africastalking.AfricasTalking;
import com.africastalking.SMSService;

public class App {

    private static SMSService sms;

    static {
        AfricasTalking.initialize(USERNAME, API_KEY);
        sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
    }

    public static void main(String[] args) {

        port(3000);

        get("/", (req, res) -> {
            return "Hello There!";
        });

        post("/register/:phone", (req, res) -> sms.send("Welcome to Awesome Company", new String[] {req.params("phone")}));

    }

}
```

**Android App**

```java

import com.africastalking.AfricasTalking;
import com.africastalking.SMSService;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String username = ... // from super secret place
        String apiKey = ... // from extra super secret place

        AfricasTalking.initialize(username, apiKey);
        SMSService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        
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
    
    // ...
    
}

```