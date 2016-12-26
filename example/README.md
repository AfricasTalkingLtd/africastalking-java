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


```