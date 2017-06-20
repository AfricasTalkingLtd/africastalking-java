# Africa's Talking Java SDK

I recommend using the client/server setup of the SDK to avoid hardcoding your username and API key into the distributed APK. The client would need a token to authenticate to the server. This token can be generated on login (or whenever your app authenticates users).

## Usage 

```java
/* On The App */

// 1. Get TOKEN out-of-band (e.g. on login)
String token = some_function();

// 2. Initialize SDK
ATClient.initialize(host, port, token);

// 3. Initialize a service e.g. SMS
SMS sms = ATClient.getSmsService();

// Use the service
boolean sent = sms.send("Hello Message!", new String[] {"2547xxxxxx"});




/* On The Server (Java) */
ATServer server = new ATServer(port, username, apiKey);
server.start();
// optionally block until server is shutdown
server.blockUntilShutdown();

// On request (e.g. login), issue a token to be used by the client
String token = server.generateToken();

// Later you can revoke the token (e.g. logout)
server.revokeToken(token);

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

