# Africa's Talking

[ ![Download](https://api.bintray.com/packages/africastalking/java/com.africastalking/images/download.svg) ](https://bintray.com/africastalking/java/com.africastalking/_latestVersion)

I recommend using the client/server config to avoid hard-coding your username and API key into the distributed APK.
The client would need a token to authenticate to the server.
This token can be generated on login (or whenever/however your app authenticates its users).

## Usage 

```java
/* On The Client (Android) */

public class SomeActivity extends Activity {

    public String some_login_function(username, password);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(args)
        setContentView(R.layout.some_activity);

        // 1. Get auth token 
        String token = some_login_function("usr", "pwd");

        // 2. Initialize client with your server's configs + token
        ATClient.initialize(HOST, PORT, token);

        // 3. Use the library e.g. checkout then send sms
        Payment payment = ATClient.getPaymentService();
        payment.checkout("AwesomeProduct", "+2547xxxxxx", Currency.KES, new Callback<String>(){
            @Override
             public void onSuccess(String response) {
                 Log.d(response); // json or xml depending on how you setup the server
             }

             @Override
             public void onFailure(Throwable throwable) {
                 Log.e(throwable.getMessage());
             }
        });

        SMS sms = ATClient.getSmsService();
        sms.send("Hello There!", new String[] {"+2547xxxxxx", "+2439xxxxxx"}, new Callback<String>() {
            @Override
             public void onSuccess(String response) {
                 Log.d(response); // json or xml depending on how you setup the server
             }

             @Override
             public void onFailure(Throwable throwable) {
                 Log.e(throwable.getMessage());
             }
        });
    }
}




/* On The Server (Java, Node.js, PHP, C/C++, C# and all languages supported by protobuf.) */

public class SomeJavaApplication {

    public static void main(String[] args) {

        // 1. Initialize the server
        ATServer mATServer = new ATServer(PORT, "USERNAME", "API_KEY");

        // 2. Start the server
        mATServer.start();


        get("/", (req, res) -> Render("Home"));

        get("/account", (req, res) -> Render("Account"));

        post("/login", (req, res) -> {
            boolean authenticated = login(req);
            if (!authenticated) {
                return res.sendUnauthorized();
            }

            String token = mATServer.
        });

        post("/logout", (req, res) -> {
            // end some user session...
            req.session.destroy();

            // Revoke their token
            String token = req.body.get("token");
            mATServer.revokeToken(token)
        });
    }
}

```

See [example](../example/) for more usage examples.

## Download

Downloadable .jars can be found on the [GitHub download page](releases).
You can also depend on the .jar through Maven (from `http://dl.bintray.com/africastalking/java`):

```xml
<dependency>
  <groupId>com.africastalking</groupId>
  <artifactId>server</artifactId>
  <version>VERSION</version>
</dependency>
```

or Gradle:

```groovy
repositories {
  maven {
    url  "http://dl.bintray.com/africastalking/java"
  }
}

dependencies{
  
  compile 'com.africastalking:client:VERSION'
  
  // Server only
  compile 'com.africastalking:server:VERSION'
  
}
```

