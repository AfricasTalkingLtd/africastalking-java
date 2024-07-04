# Africa's Talking

[![Release](https://jitpack.io/v/AfricasTalkingLtd/africastalking-java.svg)](https://jitpack.io/#AfricasTalkingLtd/africastalking-java)

>
> This SDK provides convenient access to the Africa's Talking API from apps written in Java/Kotlin/Scala with support for JDK8+.
>


## Documentation
Take a look at the [API docs here](https://developers.africastalking.com/).

## Install

You can depend on the jars through Maven (from `https://jitpack.io`):
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
...
<dependency>
  <groupId>com.github.AfricasTalkingLtd.africastalking-java</groupId>
  <artifactId>core</artifactId>
  <version>3.4.11</version>
</dependency>
```
or sbt:

```
resolvers += "jitpack" at "https://jitpack.io"
// Get all services
libraryDependencies += "com.github.AfricasTalkingLtd.africastalking-java" % "core" % "3.4.11"	
```

or Gradle (Groovy DSL):
```groovy
repositories {
  maven {
    url  "https://jitpack.io"
  }
}

dependencies{
  // Get all services
  implementation 'com.github.AfricasTalkingLtd.africastalking-java:core:3.4.11'
```

or Gradle (Kotlin DSL):
```kotlin
repositories {
    jcenter()
    maven { setUrl("https://jitpack.io") }
}

dependencies{
  // Get all services
  implementation("com.github.AfricasTalkingLtd.africastalking-java:core:3.4.11")
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
  - [Chat Service](#chatservice): `AfricasTalking.getService(AfricasTalking.SERVICE_CHAT)`
  - [Airtime Service](#airtimeservice): `AfricasTalking.getService(AfricasTalking.SERVICE_AIRTIME)`
  - [Voice Service](#voiceservice): `AfricasTalking.getService(AfricasTalking.SERVICE_VOICE)`
  - [Insights Service](#insightsservice): `AfricasTalking.getService(AfricasTalking.SERVICE_INSIGHTS)`
  - [Mobile Data Service](#mobiledataservice): `AfricasTalking.getService(AfricasTalking.SERVICE_MOBILE_DATA)`
  - [Token Service](#tokenservice): `AfricasTalking.getService(AfricasTalking.SERVICE_TOKEN)`
  - [Application Service](#applicationservice): `AfricasTalking.getService(AfricasTalking.SERVICE_APPLICATION)`

> Note on **USSD**: For more information, please read [this](https://developers.africastalking.com/docs/ussd/overview)

## Services

All methods are synchronous (i.e. will block current thread) but provide asynchronous variants that take a `com.africastalking.Callback<?>` as the last argument.

All phone numbers use the international format. e.g. `+234xxxxxxxx`.

### `ApplicationService`

- `fetchApplicationData()`: Get app information. e.g. balance

### `AirtimeService`

- `setIdempotencyKey(String key)`: Set the [**Idempotency-Key**](https://developers.africastalking.com/docs/idempotent_requests) for the next request. Call this function every time before calling `send()` if you're sending the same amount to the same phone number.

- `setMaxRetry(int retries)`: Set the maximum number of retries in case of failed airtime deliveries due to telco unavailability or any other reason.

- `send(String phoneNumber, String currencyCode, float amount)`: Send airtime to a phone number. Example amount would be `KES 150`.

- `send(HashMap<String,String> recipients)`: Send airtime to many of phone numbers. The keys in the `recipients` map are phone numbers while the values are airtime amounts. The amounts need to have currency info e.g. `UXG 4265`.

For more information about status notification, please read the airtime [documentation](https://developers.africastalking.com/docs/airtime/notifications/validation)

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

  

- `createSubscription(String shortCode, String keyword, String phoneNumber)`: Create a premium subscription.

  - `shortCode`: This is the premium short code mapped to your account.
  - `keyword`: A premium keyword under the above short code and mapped to your account.
  - `phoneNumber`:  The phone number to be subscribed

  

- `deleteSubscription(String shortCode, String keyword, String phoneNumber)`: Remove a phone number from a premium subscription.

  - `shortCode`: This is the premium short code mapped to your account.
  - `keyword`: A premium keyword under the above short code and mapped to your account.
  - `phoneNumber`: The phone number to be unsubscribed

  

For more information, read the following: 

- [How to receive SMS](https://developers.africastalking.com/docs/sms/notifications)
- [How to get notified of delivery reports](https://developers.africastalking.com/docs/sms/notifications)
- [How to listen for subscription notifications](https://developers.africastalking.com/docs/sms/notifications)

### `ChatService`

- `sendMessage(String productId, String customerNumber, Channel channel, String channelNumber, String text)`: Send a message with text.

  - `productId`: Your product Id
  - `customerNumber`: The destination, which is the phone number of the customer (WhatsApp) or the chat id (Telegram)
  - `channel`: The messaging channel that will be used to send the message. Accepted values are WhatsApp or Telegram.
  - `channelNumber`: The channel number that will be used to send out the message. Examples are a WhatsApp phone number or a Telegram username.
  - `text`: The text message to send

- `sendMessage(String productId, String customerNumber, Channel channel, String channelNumber, MediaType type, String url)`: Send a message with media.

  - `productId`: Your product Id
  - `customerNumber`: The destination, which is the phone number of the customer (WhatsApp) or the chat id (Telegram)
  - `channel`: The messaging channel that will be used to send the message. Accepted values are WhatsApp or Telegram.
  - `channelNumber`: The channel number that will be used to send out the message. Examples are a WhatsApp phone number or a Telegram username.
  - `type`: The media type. Possible values are Image, Audio, Video, Document, Voice, Sticker.
  - `url`: A valid URL location from where the media can be downloaded from.

- `sendMessage(String productId, String customerNumber, Channel channel, String channelNumber, float latitude, float longitude)`: Send a message with location.

  - `productId`: Your product Id
  - `customerNumber`: The destination, which is the phone number of the customer (WhatsApp) or the chat id (Telegram)
  - `channel`: The messaging channel that will be used to send the message. Accepted values are WhatsApp or Telegram.
  - `channelNumber`: The channel number that will be used to send out the message. Examples are a WhatsApp phone number or a Telegram username.
  - `latitude`: The latitude which should be between -90.00 and +90.00.
  - `longitude`: The longitude which should be between -90.00 and +90.00.

- `sendTemplate(String productId, String customerNumber, Channel channel, String channelNumber, Template template)`: Send a template.

  - `productId`: Your product Id
  - `customerNumber`: The destination, which is the phone number of the customer (WhatsApp) or the chat id (Telegram)
  - `channel`: The messaging channel that will be used to send the message. Accepted values are WhatsApp or Telegram.
  - `channelNumber`: The channel number that will be used to send out the message. Examples are a WhatsApp phone number or a Telegram username.
  - `template`: Specifies a registered template message to send.

- `optIn(String customerNumber, Channel channel, String channelNumber)`: Send a opt-in consent.

  - `customerNumber`: The destination, which is the phone number of the customer (WhatsApp) or the chat id (Telegram)
  - `channel`: The messaging channel that will be used to send the message. Accepted values are WhatsApp or Telegram.
  - `channelNumber`: The channel number that will be used to send out the message. Examples are a WhatsApp phone number or a Telegram username.

- `optOut(String customerNumber, Channel channel, String channelNumber)`: Send a opt-out consent.

  - `customerNumber`: The destination, which is the phone number of the customer (WhatsApp) or the chat id (Telegram)
  - `channel`: The messaging channel that will be used to send the message. Accepted values are WhatsApp or Telegram.
  - `channelNumber`: The channel number that will be used to send out the message. Examples are a WhatsApp phone number or a Telegram username.


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


For more information on voice, please read the [documentation](https://developers.africastalking.com/docs/voice/overview)

### `InsightsService`

- `checkSimSwapState(phoneNumbers: List<String>)`: Check the sim swap state of phone number(s).

For more information on insights, please read the [documentation](https://developers.africastalking.com/docs/insights/overview?preview=true)

### `MobileDataService`

- `send(String product, List<MobileDataRecipient> recipients)`: Send mobile data from a given product.

- `findTransaction(String transactionId)`: Find a mobile data transaction

- `fetchWalletBalance()`: Fetch a mobile data product balance


For more information on mobile data, please read the [documentation](https://developers.africastalking.com/docs/data/overview)


### `TokenService`

- `generateAuthToken()`: Generate an auth token to use for authentication instead of an API key.


## Development
```shell
$ git clone https://github.com/AfricasTalkingLtd/africastalking-java.git
$ cd africastalking-java
$ touch local.properties
```

Make sure your `local.properties` file has the following content then run `./gradlew build`

```ini
# AT API
api.username=sandbox
api.key=some_key
```

## Issues

If you find a bug, please file an issue on [our issue tracker on GitHub](https://github.com/AfricasTalkingLtd/africastalking-java/issues).

