# Telebirr Java SDK

A lightweight Java 17 SDK for integrating Ethio Telecom’s Telebirr H5 Web Payment into your Spring Boot or any Java application. Provides:

* Configuration via a fluent `TelebirrConfig` builder
* Payment initiation (`/payment/v1/merchant/preOrder`) with RSA-signed payloads
* Asynchronous payment notification verification and parsing
* Zero external HTTP dependencies (uses built-in `java.net.http.HttpClient`)

---

## Features

* **Java 17+** codebase using modern language features (`var`, Text Blocks)
* **Immutable configuration** via a builder pattern
* **HTTP/2 support** and configurable timeouts
* **SHA-256 with RSA** signing out-of-the-box
* **Easy Maven coordinates**:

  ```xml
  <dependency>
    <groupId>io.github.alealem</groupId>
    <artifactId>telebirr-sdk</artifactId>
    <version>1.0.0</version>
  </dependency>
  ```

---

## Installation

Add the dependency to your `pom.xml` (Maven Central or GitHub Packages):

```xml
<dependency>
  <groupId>io.github.alealem</groupId>
  <artifactId>telebirr-sdk</artifactId>
  <version>1.0.0</version>
</dependency>
```

To consume from GitHub Packages, add to your `settings.xml`:

```xml
<server>
  <id>github</id>
  <username>${env.GITHUB_ACTOR}</username>
  <password>${env.GITHUB_TOKEN}</password>
</server>
```

And include this in your `pom.xml`:

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
import io.github.alealem.telebirr.config.TelebirrConfig;
import io.github.alealem.telebirr.TelebirrClient;

TelebirrConfig config = new TelebirrConfig.Builder()
  .setBaseUrl("https://developerportal.ethiotelebirr.et:38443/payment")
  .setWebBaseUrl("https://developerportal.ethiotelebirr.et:38443/payment/web/paygate?")
  .setFabricAppId("YOUR_FABRIC_APP_ID")
  .setAppSecret("YOUR_APP_SECRET")
  .setMerchantAppId("YOUR_MERCHANT_APPID")
  .setMerchantCode("YOUR_MERCHANT_CODE")
  .setNotifyUrl("https://your-domain.com/telebirr/notify")
  .setPayeeIdentifierType("04")
  .setPayeeType("5000")
  .setRedirectUrl("https://your-domain.com/payment-result")
  .setCallbackInfo("From web")
  .setPrivateKeyPem("-----BEGIN PRIVATE KEY-----…")
  .setPublicKeyPem("-----BEGIN PUBLIC KEY-----…")
  .build();

TelebirrClient client = new TelebirrClient(config, /* insecure = */ true);
```

### 2. Initiate Payment

```java
import io.github.alealem.telebirr.model.PreOrderResponse;

PreOrderResponse response = client.createOrder("Book Purchase", "9.00");
String toPayUrl = client.buildWebPayUrl(response.getBizContent().getPrepayId());
// Redirect user to: toPayUrl
```

### 3. Handle Payment Notifications

```java
import io.github.alealem.telebirr.model.PaymentNotification;

// Parse the HTTP request body into a PaymentNotification instance
PaymentNotification notification = ...;
boolean valid = client.verifyNotification(notification);
if (valid && "Completed".equals(notification.getTradeStatus())) {
  String merchOrderId = notification.getMerchOrderId();
  String paymentOrderId = notification.getPaymentOrderId();
  // Process successful payment...
}
```

---

## Building & Publishing

* **Build**:

  ```bash
  mvn clean package
  ```
* **Publish**:

  1. Tag a release (e.g. `git tag v1.0.0 && git push origin v1.0.0`).
  2. GitHub Actions (or your CI) will deploy to Maven Central and GitHub Packages.

---

## Contributing

1. Fork the repository (`github.com/alealem/telebirr`)
2. Create a feature branch:

   ```bash
   git checkout -b feature/foo
   ```
3. Commit your changes and push:

   ```bash
   git push origin feature/foo
   ```
4. Open a Pull Request

Please follow the existing code style and include tests where appropriate.

---

## License

Apache License 2.0. See [LICENSE](LICENSE) for details.
