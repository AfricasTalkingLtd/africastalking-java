# Africa's Talking Java SDK

[ ![Download](https://api.bintray.com/packages/africastalking/java/com.africastalking/images/download.svg) ](https://bintray.com/africastalking/java/com.africastalking/_latestVersion)

> The wrapper provides convenient access to the Africa's Talking API from applications written in Java.
>
> 
> **Android Users**:
> Remember your API key has to be kept secret; hard-coding it into an apk or a jar you publish is a security risk. So on Android, use the [Android SDK](https://github.com/AfricasTalkingLtd/africastalking-android).


## Documentation
Take a look at the [API docs here](http://docs.africastalking.com).

## Install

You can depend on the .jar through Maven (from `http://dl.bintray.com/africastalking/java`):
```xml
<dependency>
  <groupId>com.africastalking</groupId>
  <artifactId>core</artifactId>
  <version>3.2.0</version>
</dependency>
```
or sbt:

```
resolvers += "africastalking maven repository" at "http://dl.bintray.com/africastalking/java"
// Get all services
libraryDependencies += "com.africastalking" % "core" % "3.2.0"
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
  compile 'com.africastalking:core:3.2.0'
}
```

## Usage

```java
// Initialize
String username = "YOUR_USERNAME";    // use 'sandbox' for development in the test environment
String apiKey = "YOUR_API_KEY";       // use your sandbox app API key for development in the test environment
AfricasTalking.initialize(username, apiKey);

// Initialize a service e.g. SMS
SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);

// Use the service
List<Recipient> response = sms.send("Hello Message!", new String[] {"2547xxxxxx"});
```

See [example](example/) for more usage examples.


## Initialization

The following static methods are available on the `AfricasTalking` class to initialize the library:

- `initialize(string username, String apiKey)`: Initialize the library.

- `setLogger(Logger)`: Set logging object.

- `getService(Service.class | AfricasTalking.SERVICE_*)`: Get an instance to a given service by name or by class.

## Services

All methods return a `String` of `json` data. All methods are synchronous (i.e. will block current thread) but provide asynchronous variants that take a `Callback<String>` as the last argument.

### `AccountService`

- `getUser()`: Get user information.

### `AirtimeService`

- `send(String phone, String amount)`: Send airtime to a phone number. Example amount would be `KES 150`.

- `send(HashMap<String,String> recipients)`: Send airtime to a bunch of phone numbers. The keys in the `recipients` map are phone numbers while the values are airtime amounts. The amounts need to have currency info e.g. `UXG 4265`.

For more information about status notification, please read [http://docs.africastalking.com/airtime/callback](http://docs.africastalking.com/airtime/callback)

### `SmsService`

- `send(String message, String[] recipients)`: Send a message

- `sendPremium(String message, String keyword, String linkId, String[] recipients)`: Send a premium SMS

- `fetchMessage()`: Fetch your messages

- `fetchSubscription(String shortCode, String keyword)`: Fetch your premium subscription data

- `createSubscription(String shortCode, String keyword, String phoneNumber, String checkoutToken)`: Create a premium subscription

For more information on: 

- How to receive SMS: [http://docs.africastalking.com/sms/callback](http://docs.africastalking.com/sms/callback)
- How to get notified of delivery reports: [http://docs.africastalking.com/sms/deliveryreports](http://docs.africastalking.com/sms/deliveryreports)
- How to listen for subscription notifications: [http://docs.africastalking.com/subscriptions/callback](http://docs.africastalking.com/subscriptions/callback)


### `PaymentService`

- `cardCheckout(String productName, String amount, PaymentCard paymentCard)`: Initiate card checkout.
- `validateCardCheckout(String transactionId, String token)`: Validate a card checkout
- `bankCheckout(String productName, String amount, BankAccount bankAccount)`: Initiate bank checkout.
- `validateBankCheckout(String transactionId, String token)`: Validate a bank checkout
- `bankTransfer(String productName, List<Bank> recipients)`: Move money form payment wallet to bank account
- `mobileCheckout(String productName, String phoneNumber, String amount)`: Initiate mobile checkout.
- `mobileB2C(String productName, List<Consumer> consumers)`: Send mobile money to consumer. 
- `mobileB2B(String productName, Business recipient)`: Send mobile money to business.


For more information, please read [http://docs.africastalking.com/payments](http://docs.africastalking.com/payments)


### `VoiceService`

- `call(String phone)`: Initiate a phone call

- `fetchQueuedCalls(String phone)`: Get queued calls

- `uploadMediaFile(String phone, String url)`: Upload voice media file

- `ActionBuilder`: Build voice xml when callback URL receives a `POST` from Africa's Talking

    - `say()`

    - `play()`

    - `getDigits()`

    - `dial()`

    - `conference()`

    - `record()`

    - `enqueue()`

    - `dequeue()`

    - `reject()`

    - `redirect()`

    - `build()`: Finally build the xml


For more information, please read [http://docs.africastalking.com/voice](http://docs.africastalking.com/voice)


### `TokenService`

- `createCheckoutToken(String phoneNumber)`: Create a new checkout token for `phoneNumber`.

- `generateAuthToken()`: Generate an auth token to use for authentication instead of an API key.

### `UssdService` *TODO?*

For more information, please read [http://docs.africastalking.com/ussd](http://docs.africastalking.com/ussd)



## Development
```shell
$ git clone https://github.com/aksalj/africastalking-java.git
$ cd africastalking-java
$ touch local.properties
```

Make sure your `local.properties` file has the following content then run `./gradlew build`

```ini
# AT API
api.username=sandbox
api.key=some_key

# Bintray
bintray.user=fake
bintray.key=fake
bintray.repo=fake
bintray.organization=fake
bintray.package=fake
bintray.groupId=fake
bintray.version=fake
bintray.vscUrl=fake
```

## Issues

If you find a bug, please file an issue on [our issue tracker on GitHub](https://github.com/AfricasTalkingLtd/africastalking-java/issues).

