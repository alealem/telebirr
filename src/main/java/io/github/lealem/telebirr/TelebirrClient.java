package io.github.lealem.telebirr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.lealem.telebirr.dto.*;
import io.github.lealem.telebirr.exception.TelebirrException;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.*;
import java.util.Base64;
import java.util.stream.Collectors;

/**
 * Telebirr H5 Web Payment client using Java 17 HttpClient and Jackson.
 */
public final class TelebirrClient {
  private final TelebirrProperties props;
  private final PublicKey telebirrPubKey;
  private final ObjectMapper mapper = new ObjectMapper();
  private final HttpClient http;

  public TelebirrClient(TelebirrProperties props) throws TelebirrException {
    this.props        = props;
    this.telebirrPubKey = loadPublicKey(props.getPublicKey());
    this.http         = HttpClient.newBuilder()
        .connectTimeout(props.getTimeout())
        .version(HttpClient.Version.HTTP_2)
        .build();
  }

  /**
   * Initiate payment; returns an H5 URL for redirection.
   */
  public PaymentResponse createPayment(PaymentRequest req) throws TelebirrException {
    try {
      var timestamp = String.valueOf(System.currentTimeMillis());
      var nonce     = UUID.randomUUID().toString().replace("-", "");

      // Build and sort parameters
      Map<String,String> p = new TreeMap<>();
      p.put("appId",          props.getAppId());
      p.put("appKey",         props.getAppKey());
      p.put("nonce",          nonce);
      p.put("notifyUrl",      props.getNotifyUrl());
      p.put("outTradeNo",     req.outTradeNo());
      p.put("receiveName",    props.getReceiveName());
      p.put("returnUrl",      props.getReturnUrl());
      p.put("shortCode",      props.getShortCode());
      p.put("subject",        req.subject());
      p.put("timeoutExpress", String.valueOf(props.getTimeout().toMinutes()));
      p.put("timestamp",      timestamp);
      p.put("totalAmount",    req.totalAmount());

      // Compute SHA-256 signature
      String stringA = p.entrySet().stream()
          .map(e -> e.getKey() + "=" + e.getValue())
          .collect(Collectors.joining("&"));
      var sign = Base64.getEncoder().encodeToString(
          java.security.MessageDigest
            .getInstance("SHA-256")
            .digest(stringA.getBytes(StandardCharsets.UTF_8))
      ).toUpperCase();

      // Prepare payload (exclude appKey)
      ObjectNode payload = mapper.createObjectNode();
      p.forEach((k,v) -> { if (!k.equals("appKey")) payload.put(k, v); });

      // Encrypt payload with RSA/ECB/PKCS1Padding
      byte[] plain = mapper.writeValueAsBytes(payload);
      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      cipher.init(Cipher.ENCRYPT_MODE, telebirrPubKey);
      var bout = new ByteArrayOutputStream();
      for (int offset = 0; offset < plain.length; offset += 245) {
        int len = Math.min(245, plain.length - offset);
        bout.write(cipher.doFinal(plain, offset, len));
      }
      String data = Base64.getEncoder().encodeToString(bout.toByteArray());

      // Build request JSON
      ObjectNode body = mapper.createObjectNode();
      body.put("appId", props.getAppId());
      body.put("data",  data);
      body.put("sign",  sign);
      String url = String.format("%s/payment/v1/merchant/preOrder", props.getBaseUrl());

      var request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .timeout(props.getTimeout())
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
          .build();

      var response = http.send(request, HttpResponse.BodyHandlers.ofString());
      var root = mapper.readTree(response.body());
      if ("0".equals(root.path("code").asText())) {
        var toPay = root.path("data").path("toPayUrl").asText();
        return new PaymentResponse(toPay);
      }
      throw new TelebirrException("Telebirr error: " + root.path("msg").asText());

    } catch (TelebirrException e) {
      throw e;
    } catch (Exception e) {
      throw new TelebirrException("createPayment failed", e);
    }
  }

  /**
   * Decrypts and parses async notification.
   */
  public NotificationResponse parseNotification(NotificationRequest req)
      throws TelebirrException {
    try {
      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
      cipher.init(Cipher.DECRYPT_MODE, telebirrPubKey);
      var plain = cipher.doFinal(Base64.getDecoder().decode(req.data()));
      var node  = mapper.readTree(plain);
      return new NotificationResponse(
          node.path("outTradeNo").asText(),
          node.path("transactionNo").asText(),
          node.path("tradeNo").asText(),
          node.path("msisdn").asText(),
          node.path("totalAmount").asText(),
          node.path("tradeDate").asText()
      );
    } catch (Exception e) {
      throw new TelebirrException("parseNotification failed", e);
    }
  }

  private PublicKey loadPublicKey(String base64Key) throws TelebirrException {
    try {
      var decoded = Base64.getDecoder().decode(base64Key);
      var spec    = new X509EncodedKeySpec(decoded);
      return KeyFactory.getInstance("RSA").generatePublic(spec);
    } catch (Exception e) {
      throw new TelebirrException("Invalid public key", e);
    }
  }
}