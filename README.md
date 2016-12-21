# Africa's Talking Java SDK



## Usage

```java

// Initialize SDK
AfricasTalking.initialize(USERNAME, API_KEY);

// Initialize a service e.g. SMS
SMSService sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);

// Use the service
boolean sent = sms.send(new String[] {"2547xxxxxx"}, "App", "Hello Message!");


```



## Download

Downloadable .jars can be found on the [GitHub download page][2].
You can also depend on the .jar through Maven:
```xml
<dependency>
  <groupId>com.africastalking</groupId>
  <artifactId>core</artifactId>
  <version>1.0.0</version>
</dependency>
```
or Gradle:
```groovy
// Get all services
compile 'com.africastalking:core:1.0.0'

// or get individual services
compile 'com.africastalking:account:1.0.0'
compile 'com.africastalking:payments:1.0.0'
compile 'com.africastalking:airtime:1.0.0'
compile 'com.africastalking:voice:1.0.0'
compile 'com.africastalking:sms:1.0.0'
compile 'com.africastalking:ussd:1.0.0'

```

## Services

### `AccountService`

- `fetchUser()`: Get user info. Async version available as `fetchUser(Callback<String>)`.

### `AirtimeService`

- `send()`

### `SMSService`

- `send()`

- `sendBulk()`

- `sendPremium()`

- `fetchMessage()`

- `fetchSubscription()`

- `createSubscription()`



### `USSDService`

- `END`

- `CON`

### `VoiceService`

- `call()`

- `fetchQueuedCalls()`

- `ResponseBuilder`:

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
    - `build()`

### `PaymentService`

- `checkout()`

- `transfer()`

- `balance()`
