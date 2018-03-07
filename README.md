# Africa's Talking Java SDK

[ ![Download](https://api.bintray.com/packages/africastalking/java/com.africastalking/images/download.svg) ](https://bintray.com/africastalking/java/com.africastalking/_latestVersion)

> The SDK provides convenient access to the Africa's Talking API from applications written in Java.
>
> 
> **Android Users**:
> Remember your API key has to be kept secret; hard-coding it into an apk you publish is a security risk. So on Android, use the [Android SDK](https://github.com/AfricasTalkingLtd/africastalking-android) instead.


## Documentation
Take a look at the [API docs here](http://docs.africastalking.com).

## Install

You can depend on the .jar through Maven (from `http://dl.bintray.com/africastalking/java`):
```xml
<dependency>
  <groupId>com.africastalking</groupId>
  <artifactId>core</artifactId>
  <version>3.3.3</version>
</dependency>
```
or sbt:

```
resolvers += "africastalking maven repository" at "http://dl.bintray.com/africastalking/java"
// Get all services
libraryDependencies += "com.africastalking" % "core" % "3.3.3"
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
  compile 'com.africastalking:core:3.3.3'
}
```

## Usage

The SDK needs to be initialized with your app username and API key, which you get from the [dashboard](https://account/africastalking.com).

> You can use this SDK for either production or sandbox apps. For sandbox, the app username is **ALWAYS** `sandbox`

```java
// Initialize
String username = "YOUR_USERNAME";    // use 'sandbox' for development in the test environment
String apiKey = "YOUR_API_KEY";       // use your sandbox app API key for development in the test environment
AfricasTalking.initialize(username, apiKey);

// Initialize a service e.g. SMS
SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);

// Use the service
List<Recipient> response = sms.send("Hello Message!", new String[] {"+2547xxxxxx"});
```

See [example](example/) for more usage examples.


## Initialization

The following static methods are available on the `AfricasTalking` class to initialize the library:

- `initialize(string username, String apiKey)`: Initialize the library.

- `setLogger(Logger)`: Set logging object.

- `getService(Service.class | AfricasTalking.SERVICE_*)`: Get an instance to a given service by name or by class:

  - [Account](#accountservice): `AfricasTalking.getService(AfricasTalking.SERVICE_ACCOUNT)`
  - [Airtime](#airtimeservice): `AfricasTalking.getService(AfricasTalking.SERVICE_AIRTIME)`
  - [SMS](#smsservice): `AfricasTalking.getService(AfricasTalking.SERVICE_SMS)`
  - [Payments](#paymentservice): `AfricasTalking.getService(AfricasTalking.SERVICE_PAYMENT)`
  - [Voice](#voiceservice): `AfricasTalking.getService(AfricasTalking.SERVICE_VOICE)`
  - [Token](#tokenservice): `AfricasTalking.getService(AfricasTalking.SERVICE_TOKEN)`
  - [USSD](#ussdservice): `AfricasTalking.getService(AfricasTalking.SERVICE_USSD)`

## Services

All methods are synchronous (i.e. will block current thread) but provide asynchronous variants that take a `com.africastalking.Callback<String>` as the last argument.

All phone numbers use the international format. e.g. `+234xxxxxxxx`.

All **amount strings** contain currency code as well. e.g. `UGX 443.88`.

### `AccountService`

- `fetchAccount()`: Get app information. e.g. balance

### `AirtimeService`

- `send(String phoneNumber, String amount)`: Send airtime to a phone number. Example amount would be `KES 150`.

- `send(HashMap<String,String> recipients)`: Send airtime to a bunch of phone numbers. The keys in the `recipients` map are phone numbers while the values are airtime amounts. The amounts need to have currency info e.g. `UXG 4265`.

For more information about status notification, please read [http://docs.africastalking.com/airtime/callback](http://docs.africastalking.com/airtime/callback)

### `SmsService`

- `send(String message, String[] recipients)`: Send a message

- `sendPremium(String message, String keyword, String linkId, String[] recipients)`: Send a premium SMS

- `fetchMessages()`: Fetch your messages

- `fetchSubscription(String shortCode, String keyword)`: Fetch your premium subscription data

- `createSubscription(String shortCode, String keyword, String phoneNumber, String checkoutToken)`: Create a premium subscription

For more information on: 

- How to receive SMS: [http://docs.africastalking.com/sms/callback](http://docs.africastalking.com/sms/callback)
- How to get notified of delivery reports: [http://docs.africastalking.com/sms/deliveryreports](http://docs.africastalking.com/sms/deliveryreports)
- How to listen for subscription notifications: [http://docs.africastalking.com/subscriptions/callback](http://docs.africastalking.com/subscriptions/callback)


### `PaymentService`

- `cardCheckout(String productName, String amount, PaymentCard paymentCard, String narration, Map metadata)`: Initiate card checkout.

- `validateCardCheckout(String transactionId, String otp)`: Validate a card checkout

- `bankCheckout(String productName, String amount, BankAccount bankAccount, String narration, Map metadata)`: Initiate bank checkout.

- `validateBankCheckout(String transactionId, String otp)`: Validate a bank checkout

- `bankTransfer(String productName, List<Bank> recipients)`: Move money form payment wallet to bank account

- `mobileCheckout(String productName, String phoneNumber, String amount)`: Initiate mobile checkout.

- `mobileB2C(String productName, List<Consumer> consumers)`: Send mobile money to consumer. 

- `mobileB2B(String productName, Business recipient)`: Send mobile money to business.


For more information, please read [http://docs.africastalking.com/payments](http://docs.africastalking.com/payments)


### `VoiceService`

- `call(String phoneNumber)`: Initiate a phone call

- `fetchQueuedCalls(String phoneNumber)`: Get queued calls

- `uploadMediaFile(String phoneNumber, String url)`: Upload voice media file

- `ActionBuilder`: Build voice xml when callback URL receives a `POST` from Africa's Talking

    - `say()`: Add a `Say` action.

    - `play()`: Add a `Play` action.

    - `getDigits()`: Add a `GetDigits` action.

    - `dial()`: Add a `Dial` action.

    - `conference()`: Add a `Conferemce` action.

    - `record()`: Add a `Record` action.

    - `enqueue()`: Add a `Enqueue` action.

    - `dequeue()`: Add a `Dequeue` action.

    - `reject()`: Add a `Reject` action.

    - `redirect()`: Add a `Redirect` action.

    - `build()`: Finally build the xml


For more information, please read [http://docs.africastalking.com/voice](http://docs.africastalking.com/voice)


### `TokenService`

- `createCheckoutToken(String phoneNumber)`: Create a new checkout token for `phoneNumber`.

- `generateAuthToken()`: Generate an auth token to use for authentication instead of an API key.

### `UssdService`

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

