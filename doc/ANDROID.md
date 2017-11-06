# Africa's Talking

[ ![Download](https://api.bintray.com/packages/africastalking/android/com.africastalking/images/download.svg) ](https://bintray.com/africastalking/android/com.africastalking/_latestVersion)

**Important**: Checkout the [android repo](https://github.com/AfricasTalkingLtd/africastalking-android) for more information on how to use Africa's Talking APIs in your Android project. [https://github.com/AfricasTalkingLtd/africastalking-android](https://github.com/AfricasTalkingLtd/africastalking-android)

The Android SDK simplifies the integration of Africa's Talking APIs into your Android apps. For better security,
the SDK is split into two components: A **server** module that stores API keys, SIP credentials and other secrets.
And a **client** module that runs in your app. This client module gets secrets from the server component (via RPC), and uses them to interact with the various APIs.

For instance, to send an SMS, the client with request a token from the server; The server will use its API key to request a token from Africa's Talking on behalf of the client. It will then forward the token to the client which will use it to request the SMS API to send a text. All in a split second!


### Usage

Your server application could be something like this:

```java
/* On The Server (Java, Node.js, PHP, C/C++, C# and all languages supported by gRPC.) */
public class SomeJavaApplication {

    public static void main(String[] args) {
    
        // Initialize the server
        Africastalking.initialize(USERNAME, API_KEY);
        Server server = new Server();
        
        // (optional) Add SIP credentials (Voice Only)
        server.addSipCredentials(SIP_USERNAME, SIP_PASSWORD, SIP_HOST);
        
        // Start the server
        server.start();
    }
}
```

And your Android app:

```java
/* On The Client (Android) */
public class SomeActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(args);
        setContentView(R.layout.some_activity);
        
        try {
            // Init SDK
            AfricasTalking.initialize(SERVER_HOSTNAME);

            // Get Service
            AirtimeService airtime = AfricasTalking.getAirtimeService();

            // Use Service
            airtime.send("+25467675655", "KES", 100, new Callback<AirtimeResponses>() {
              @Override
              void onSuccess(AirtimeResponses responses) {
                //...
              }

              @Override
              void onError(Throwable throwable) {
                //...
              }
            });
        
        } catch (IOException ex) {
            // grrr
        }
    }
}
```

See the [example](./example) for complete sample apps (Android, Web Java+Node)

### Download

#### Server

**Node**

```shell
npm install --save africastalking
```

**Java**
```groovy
repositories {
  maven {
    url  "http://dl.bintray.com/africastalking/java"
  }
}
dependencies{
  compile 'com.africastalking:server:VERSION'
}
```

Or Maven (from `http://dl.bintray.com/africastalking/java`)

```xml
<dependency>
  <groupId>com.africastalking</groupId>
  <artifactId>server</artifactId>
  <version>VERSION</version>
</dependency>
```


#### Client (Android)
```groovy
repositories {
  maven {
    url  "http://dl.bintray.com/africastalking/android"
  }
}
dependencies{
  compile 'com.africastalking:client:VERSION'
  // or
  compile 'com.africastalking:client-ui:VERSION' // with checkout UI for payment
}
```


## Initialization
The following static methods are available on the `AfricasTalking` class to initialize the library:

- `initialize(HOST, POST)`: Initialize the library.
- `getXXXService()`: Get an instance to a given `XXX` service. e.g. `AfricasTalking.getSmsService()`, `AfricasTalking.getPaymentService()`, etc.


## Services

Methods on all services are synchronous (i.e. will block current thread) but provide asynchronous variants that take a `Callback<T>` as the last argument.

- Account
- Airtime
- SMS
- Token
- Voice
- Payment

**Note on Voice**: Unlike other services, the `VoiceService` is initialized as follows:

```java
AfricasTalking.initializeVoiceService(Context cxt, RegistrationListener listener, new Callback<VoiceService>() {
    @Override
    public void onSuccess(VoiceService service) {
      // keep a reference to the 'service'
    }

    @Override
    public void onFailure(Throwable throwable) {
      // something blew up
    }
});
```


## Requirements

On Android, This SDK requires **API 16+**. Your app will also need the following permissions:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.INTERNET" />
    
    <!-- The following are required if you want use the voice service -->
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
    
    <!-- ... -->
    
</manifest>
```

For more info, please visit [https://www.africastalking.com](https://www.africastalking.com)
