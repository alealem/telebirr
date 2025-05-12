# Telebirr Java SDK

A lightweight Java 17 SDK for integrating Ethio Telecom’s Telebirr H5 Web Payment into your Spring Boot or any Java application. Provides:

- Configuration via a fluent `TelebirrProperties` builder  
- Payment initiation (`/payment/v1/merchant/preOrder`) with RSA-encrypted payloads  
- Asynchronous notification decryption and parsing  
- Zero external HTTP dependencies (uses built-in `java.net.http.HttpClient`)

---

## Features

- **Java 17+** codebase using modern language features (records, var, Text Blocks)  
- **Immutable configuration** via a builder pattern  
- **HTTP/2 support** and configurable timeouts  
- **SHA-256** signing and **RSA/ECB/PKCS1Padding** encryption out-of-the-box  
- **Easy Maven coordinates**: `io.github.alealem:telebirr:1.0.0`

---

## Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>io.github.alealem</groupId>
  <artifactId>telebirr</artifactId>
  <version>1.0.0</version>
</dependency>
```

If you’re consuming from GitHub Packages, configure your `settings.xml`:

```xml
<server>
  <id>github</id>
  <username>${env.GITHUB_ACTOR}</username>
  <password>${env.GITHUB_TOKEN}</password>
</server>
```

And in your `pom.xml`:

```xml
<repository>
  <id>github</id>
  <url>https://maven.pkg.github.com/alealem/telebirr</url>
</repository>
```

---

## Quick Start

### 1. Configure

```java
TelebirrProperties props = TelebirrProperties.builder()
  .baseUrl("https://196.188.120.3:38443/apiaccess/payment/gateway")
  .appId("YOUR_APP_ID")
  .appKey("YOUR_APP_KEY")
  .shortCode("YOUR_SHORT_CODE")
  .publicKey("BASE64_RSA_PUBLIC_KEY")
  .notifyUrl("https://your-domain.com/telebirr/notify")
  .returnUrl("https://your-domain.com/payment-result")
  .receiveName("Your Company")
  .timeout(Duration.ofMinutes(30))
  .build();

TelebirrClient client = new TelebirrClient(props);
```

### 2. Initiate Payment

```java
PaymentRequest req = new PaymentRequest(
  "ORDER1234",                     // outTradeNo
  "Book Purchase",                 // subject
  "9.00"                           // totalAmount
);
PaymentResponse resp = client.createPayment(req);
// Redirect user to Telebirr H5 page:
String toPayUrl = resp.toPayUrl();
```

### 3. Handle Notifications

```java
NotificationRequest notifReq = ...; // parse JSON body
NotificationResponse notif = client.parseNotification(notifReq);
// notif.outTradeNo(), notif.transactionNo(), etc.
```

---

## Building & Publishing

- **Build**: `mvn clean package`  
- **Publish**: push a Git tag `vX.Y.Z`, GitHub Actions will deploy to Maven Central and GitHub Packages.

---

## Contributing

1. Fork the repo: `github.com/alealem/telebirr`  
2. Create a feature branch: `git checkout -b feature/foo`  
3. Commit your changes and push  
4. Open a Pull Request

---

## License

Apache License 2.0. See [LICENSE](LICENSE) for details.
