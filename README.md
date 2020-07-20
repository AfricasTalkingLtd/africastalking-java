# Africa's Talking Java SDK

[ ![Download](https://api.bintray.com/packages/africastalking/java/com.africastalking/images/download.svg) ](https://bintray.com/africastalking/java/com.africastalking/_latestVersion)

> The SDK provides convenient access to the Africa's Talking API from applications written in Java.
>
> 
> **Android Users**:
> Remember your API key has to be kept secret; hard-coding it into an apk you publish is a security risk. So on Android, use the [Android SDK](https://github.com/AfricasTalkingLtd/africastalking-android) instead.


## Documentation
Take a look at the [API docs here](https://build.at-labs.io/docs/getting_started%2Fintroduction).

## Install

You can depend on the [.jar](http://dl.bintray.com/africastalking/java/com/africastalking/core) through Maven (from `http://dl.bintray.com/africastalking/java`):
```xml
<repositories>
   <repository>
      <id>africastalking</id>
      <name>AfricasTalking</name>
      <url>http://dl.bintray.com/africastalking/java</url>
   </repository>
</repositories>
...
<dependency>
  <groupId>com.africastalking</groupId>
  <artifactId>core</artifactId>
  <version>3.4.4</version>
</dependency>
```
or sbt:

```
resolvers += "africastalking maven repository" at "http://dl.bintray.com/africastalking/java"
// Get all services
libraryDependencies += "com.africastalking" % "core" % "3.4.4"
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
  compile 'com.africastalking:core:3.4.4'
}
```

## Usage

The SDK needs to be initialized with your app username and API key, which you get from the [dashboard](https://account.africastalking.com).

> You can use this SDK for either production or sandbox apps. For sandbox, the app username is **ALWAYS** `sandbox`

```java
// Initialize
String username = "YOUR_USERNAME";    // use 'sandbox' for development in the test environment
String apiKey = "YOUR_API_KEY";       // use your sandbox app API key for development in the test environment
AfricasTalking.initialize(username, apiKey);

// Initialize a service e.g. SMS
SmsService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);

// Use the service
List<Recipient> response = sms.send("Hello Message!", new String[] {"+2547xxxxxx"}, true);
```

See [example](example/) for more usage examples.


## Initialization

The following static methods are available on the `AfricasTalking` class to initialize the library:

- `initialize(string username, String apiKey)`: Initialize the library.

- `setLogger(Logger)`: Set logging object.

- `getService(Service.class | AfricasTalking.SERVICE_*)`: Get an instance to a given service by name or by class:

  - [SMS Service](#smsservice): `AfricasTalking.getService(AfricasTalking.SERVICE_SMS)`
  - [Airtime Service](#airtimeservice): `AfricasTalking.getService(AfricasTalking.SERVICE_AIRTIME)`
  - [Payment Service](#paymentservice): `AfricasTalking.getService(AfricasTalking.SERVICE_PAYMENT)`
  - [Voice Service](#voiceservice): `AfricasTalking.getService(AfricasTalking.SERVICE_VOICE)`
  - [Token Service](#tokenservice): `AfricasTalking.getService(AfricasTalking.SERVICE_TOKEN)`
  - [Application Service](#applicationservice): `AfricasTalking.getService(AfricasTalking.SERVICE_APPLICATION)`

> Note on **USSD**: For more information, please read [this](https://build.at-labs.io/docs/ussd%2Foverview)

## Services

All methods are synchronous (i.e. will block current thread) but provide asynchronous variants that take a `com.africastalking.Callback<?>` as the last argument.

All phone numbers use the international format. e.g. `+234xxxxxxxx`.

### `ApplicationService`

- `fetchApplicationData()`: Get app information. e.g. balance

### `AirtimeService`

- `setIdempotencyKey(String key)`: Set the [**Idempotency-Key**](https://build.at-labs.io/docs/getting_started%2Fidempotent_requests) for the next request. Call this function every time before calling `send()` if you're sending the same amount to the same phone number.

- `send(String phoneNumber, String currencyCode, float amount)`: Send airtime to a phone number. Example amount would be `KES 150`.

- `send(HashMap<String,String> recipients)`: Send airtime to many of phone numbers. The keys in the `recipients` map are phone numbers while the values are airtime amounts. The amounts need to have currency info e.g. `UXG 4265`.

For more information about status notification, please read the airtime [documentation](https://build.at-labs.io/docs/airtime%2Fnotifications%2Fvalidation)

### `SmsService`

- `send(String message, String from, String[] recipients, boolean enqueue)`: Send a message.

  - `message`: SMS content
  - `from`: Short code or alphanumeric ID that is registered with Africa's Talking account.
  - `recipients`: An array of phone numbers.
  - `enqueue`: Set to true if you would like to deliver as many messages to the API without waiting for an acknowledgement from telcos.

  

- `sendPremium(String message, String keyword, String linkId, long retryDurationInHours, String[] recipients)`: Send a premium SMS

  - `message`: SMS content
  - `keyword`: You premium product keyword
  - `linkId`: "[...] We forward the `linkId` to your application when the user send a message to your service"
  - `retryDurationInHours`: "It specifies the number of hours your subscription message should be retried in case it's not delivered to the subscriber"
  - `recipients`: An array of phon numbers.

  

- `fetchMessages(long lastReceivedId)`: Fetch your messages

  - `lastReceivedId`: "This is the id of the message that you last processed". Defaults to `0`

  

- `fetchSubscriptions(String shortCode, String keyword, long lastReceivedId)`: Fetch your premium subscription data.

  - `shortCode`: This is the premium short code mapped to your account.
  - `keyword`: A premium keyword under the above short code and mapped to your account.
  - `lastReceivedId`: "This is the id of the message that you last processed". Defaults to `0`

  

- `createSubscription(String shortCode, String keyword, String phoneNumber, String checkoutToken)`: Create a premium subscription.

  - `shortCode`: This is the premium short code mapped to your account.
  - `keyword`: A premium keyword under the above short code and mapped to your account.
  - `phoneNumber`:  The phone number to be subscribed
  - `checkoutToken`: This is a token used to validate the subscription request. See [`TokenService`](#TokenService)

  

- `deleteSubscription(String shortCode, String keyword, String phoneNumber)`: Remove a phone number from a premium subscription.

  - `shortCode`: This is the premium short code mapped to your account.
  - `keyword`: A premium keyword under the above short code and mapped to your account.
  - `phoneNumber`: The phone number to be unsubscribed

  

For more information, read the following: 

- [How to receive SMS](https://build.at-labs.io/docs/sms%2Fnotifications)
- [How to get notified of delivery reports](https://build.at-labs.io/docs/sms%2Fnotifications)
- [How to listen for subscription notifications](https://build.at-labs.io/docs/sms%2Fnotifications)


### `PaymentService`

- `cardCheckoutCharge(String productName, String currencyCode, float amount, PaymentCard paymentCard, String narration, Map metadata)`: Initiate card checkout charge.

  - `productName`: Your payment product
  - `currencyCode`: Currency code e.g. NGN
  - `amount`: Amount to charge
  - `paymentCard`: Card to charge. See [PaymentCard](#) class.
  - `narration`: Checkout description
  - `metadata`: Additional info to go with the checkout

  

- `cardCheckoutValidate(String transactionId, String otp)`: Validate a card checkout

  - `transactionId`: Transaction ID returned on charge request
  - `otp`: A user-provided OTP

  

- `bankCheckoutCharge(String productName, String currencyCode, float amount, BankAccount bankAccount, String narration, Map metadata)`: Initiate bank checkout.

  - `productName`: Your payment product
  - `currencyCode`: Currency code e.g. NGN
  - `amount`: Amount to charge
  - `bankAccount`: Bank account to charge. See [BankAccount](#) class.
  - `narration`: Checkout description
  - `metadata`: Additional info to go with the checkout

  

- `bankCheckoutValidate(String transactionId, String otp)`: Validate a bank checkout

  - `transactionId`: Transaction ID returned on charge request
  - `otp`: A user-provided OTP

  

- `bankTransfer(String productName, List<Bank> recipients)`: Move money form payment wallet to bank account

  - `productName`: Your payment product
  - `recipients`: A list of banks to transfer to. See [Bank](#) class

  

- `mobileCheckout(String productName, String phoneNumber, String currencyCode, float amount, String providerChannel)`: Initiate mobile checkout.

  - `productName`: Your payment product
  - `phoneNumber`: Mobile wallet to charge
  - `currencyCode`: Currency code e.g. KES
  - `amount`: Amount to charge
  - `providerChannel`: The provider channel the payment will be initiated from e.g a paybill number. `OPTIONAL`

  

- `mobileB2C(String productName, List<Consumer> recipients)`: Send mobile money to consumer. 

  - `productName`: Your payment product
  - `recipients`: A list of consumers that will receive the money. See [Consumer](#) class.

  

- `mobileB2B(String productName, Business recipient)`: Send mobile money to business.

  - `productName`: Your payment product
  - `recipient`: A business recipient of the money. See [Business](#) class
  

- `mobileData(String productName, List<MobileDataRecipient> recipients)`: Send mobile data to recipients.

  - `productName`: Your payment product
  - `recipients`: A list of phone users that will receive the mobile data. See [MobileDataRecipient](#) class.

  
- `walletTransfer(String productName, long targetProductCode, String currencyCode, float amount, HashMap<String, String> metadata)`: Move money form one payment product to another.

  - `productName`: Your payment product
  - `targetProductCode`: ID of recipient payment product on Africa's Talking
  - `currencyCode`: Currency code e.g. RWF
  - `amount`: Amount to transfer
  - `metadata`: Additional info to go with the transfer

  

- `topupStash(String productName, String currencyCode, float amount, HashMap<String, String> metadata)`: Move money from payment product to app's stash.

  - `productName`: Your payment product
  - `currencyCode`: Currency code e.g. KES
  - `amount`: Amount to transfer
  - `metadata`: Additional info to go with the transfer

  

- `fetchProductTransactions(String productName, HashMap<String, String> filters)`: Fetch payment product transactions.

  - `productName`: Your payment product
  - `filters`: Query filters. Includes:

    - `pageNumber`: Page number to fetch results from. Starts from `1`. `REQUIRED`
    - `count`:  Number of results to fetch. `REQUIRED`
    - `startDate`: Start Date to consider when fetching.
    - `endDate`: End Date to consider when fetching.
    - `category`: Category to consider when fetching.
    - `prodiver`: Provider to consider when fetching.
    - `status`: Status to consider when fetching.
    - `source`: Source to consider when fetching.
    - `destination`: Destination to consider when fetching.
    - `providerChannel`: Provider channel to consider when fetching.



- `findTransaction(String transactionId)`: Find a particular transaction.



- `fetchWalletTransactions(HashMap<String, String> filters)`: Fetch wallet transactions.

  - `filters`: Query filter. Includes:
    - `pageNumber`: Page number to fetch results from. Starts from `1`. `REQUIRED`
    - `count`: Number of results to fetch. `REQUIRED`
    - `startDate`: Start Date to consider when fetching.
    - `endDate`: End Date to consider when fetching.
    - `categories`: Comma delimited list of categories to consider when fetching.


- `fetchWalletBalance()`: Fetch your wallet's balance




For more information, please read the [payments documentation](https://build.at-labs.io/docs/payments%2Foverview).


### `VoiceService`

- `call(String phoneNumber)`: Initiate a phone call

    - `phoneNumber`: Phone number to call

    

- `fetchQueuedCalls(String phoneNumber)`: Get queued calls on a phone number.

    - `phoneNumber`: Your Africa's Talking issued virtual phone number

    

- `uploadMediaFile(String phoneNumber, String url)`: Upload voice media file

    - `phoneNumber`: Your Africa's Talking issued virtual phone number
    - `url`: URL to your media file.

    

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


For more information on voice, please read the [documentation](https://build.at-labs.io/docs/voice%2Foverview)


### `TokenService`

- `createCheckoutToken(String phoneNumber)`: Create a new checkout token for `phoneNumber`.

- `generateAuthToken()`: Generate an auth token to use for authentication instead of an API key.


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

