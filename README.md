# Africa's Talking Java SDK

[ ![Download](https://api.bintray.com/packages/africastalking/java/com.africastalking/images/download.svg?version=1.1.1) ](https://bintray.com/africastalking/java/com.africastalking/1.1.1/link)

## Usage

```java

// Initialize SDK
AfricasTalking.initialize(USERNAME, API_KEY);
AfricasTalking.setEnvironment(Environment.PRODUCTION); // Environment.SANDBOX by default

// Initialize a service e.g. SMS
SMSService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);

// Use the service
boolean sent = sms.send("Hello Message!", new String[] {"2547xxxxxx"});

```

See [example](example/) for more usage examples.


**Important**:

- Remember your API key has to be kept secret, so hard-coding it into an apk or a jar you publish is a security risk, for usage on Android, [read this](doc/ANDROID.md).

- The old `AfricasTalkingGateway` class is still available, but is being phased out! :)

## Download

Downloadable .jars can be found on the [GitHub download page](releases).
You can also depend on the .jar through Maven (from `http://dl.bintray.com/africastalking/java`):
```xml
<dependency>
  <groupId>com.africastalking</groupId>
  <artifactId>core</artifactId>
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
  // Get all services
  compile 'com.africastalking:core:VERSION'
  
  // or get individual services
  compile 'com.africastalking:account:VERSION'
  compile 'com.africastalking:payments:VERSION'
  compile 'com.africastalking:airtime:VERSION'
  compile 'com.africastalking:voice:VERSION'
  compile 'com.africastalking:sms:VERSION'
  compile 'com.africastalking:ussd:VERSION'
}
```

## Initialization

The following static methods are available on the `AfricasTalking` class to initialize the SDK:

- `initialize(USERNAME, API_KEY, Format = XML, Currency = KES)`: Initialize SDK.

- `setEnvironment(Environment = PRODUCTION)`: Define environment. Can either be `Environment.SANDBOX` or `Environment.PRODUCTION` (by default)

- `enableLogging(bool)`: Enable or disable request logging.

- `setLogger(Logger)`: Set logging object.

- `getService(Service.class | AfricasTalking.SERVICE_*)`: Get an instance to a given service by name or by class.

## Services

All methods return a `String` of `xml` or `json` data based on the specified format. All methods are synchronous (i.e. will block current thread) but provide asynchronous variants that take a `Callback<String>` as the last argument.

### `AccountService`

- `getUser()`: Get user information.

### `AirtimeService`

- `send(String phone, float amount)`: Send airtime to a phone number.

- `send(HashMap<String,Float> recipients)`: Send airtime to a bunch of phone numbers. The keys in the `recipients` map are phone numbers while the values are aitrime amounts.

For more information about status notification, please read [http://docs.africastalking.com/airtime/callback](http://docs.africastalking.com/airtime/callback)

### `SMSService`

- `send(String message, String[] recipients)`: Send a message

- `sendBulk(String message, String[] recipients)`: Send a message in bulk

- `sendPremium(String message, String keyword, String linkId, String[] recipients)`: Send a premium SMS

- `fetchMessage()`: Fetch your messages

- `fetchSubscription(String shortCode, String keyword)`: Fetch your premium subscription data

- `createSubscription(String shortCode, String keyword, String phoneNumber)`: Create a premium subscription

For more information on: 

- How to receive SMS: [http://docs.africastalking.com/sms/callback](http://docs.africastalking.com/sms/callback)
- How to get notified of delivery reports: [http://docs.africastalking.com/sms/deliveryreports](http://docs.africastalking.com/sms/deliveryreports)
- How to listen for subscription notifications: [http://docs.africastalking.com/subscriptions/callback](http://docs.africastalking.com/subscriptions/callback)


### `PaymentsService`

- `checkout(String productName, String phoneNumber, float amount, Currency currency)`: Initiate mobile checkout.

- `payCustomer(String productName, Consumer recipient)`: Send money to consumer. 

- `payBusiness(String productName, Business recipient)`: Send money to business.


For more information, please read [http://docs.africastalking.com/payments](http://docs.africastalking.com/payments)


### VoiceService

- `call(String phone)`: Initiate a phone call

- `fetchQueuedCalls(String phone)`: Get queued calls

- `VoiceMessage.Builder`: Build voice xml when callback URL receives a `POST` from Africa's Talking

    - `say()`:
    - `play()`:
    - `getDigits()`:
    - `dial()`:
    - `conference()`:
    - `record()`:
    - `enqueue()`:
    - `dequeue()`:
    - `reject()`:
    - `redirect()`:
    - `build()`: Build xml


For more information, please read [http://docs.africastalking.com/voice](http://docs.africastalking.com/voice)
​    
### `USSDService` *TODO*

For more information, please read [http://docs.africastalking.com/ussd](http://docs.africastalking.com/ussd)

