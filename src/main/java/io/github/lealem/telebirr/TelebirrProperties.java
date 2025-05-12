package io.github.lealem.telebirr;

import java.time.Duration;
import java.util.Objects;

/**
 * Immutable configuration for Telebirr H5 Web Payment SDK.
 */
public final class TelebirrProperties {
  private final String baseUrl;
  private final String appId;
  private final String appKey;
  private final String shortCode;
  private final String publicKey;
  private final String notifyUrl;
  private final String returnUrl;
  private final String receiveName;
  private final Duration timeout;

  private TelebirrProperties(Builder b) {
    this.baseUrl     = Objects.requireNonNull(b.baseUrl,     "baseUrl");
    this.appId       = Objects.requireNonNull(b.appId,       "appId");
    this.appKey      = Objects.requireNonNull(b.appKey,      "appKey");
    this.shortCode   = Objects.requireNonNull(b.shortCode,   "shortCode");
    this.publicKey   = Objects.requireNonNull(b.publicKey,   "publicKey");
    this.notifyUrl   = Objects.requireNonNull(b.notifyUrl,   "notifyUrl");
    this.returnUrl   = Objects.requireNonNull(b.returnUrl,   "returnUrl");
    this.receiveName = Objects.requireNonNull(b.receiveName, "receiveName");
    this.timeout     = Objects.requireNonNull(b.timeout,     "timeout");
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String baseUrl;
    private String appId;
    private String appKey;
    private String shortCode;
    private String publicKey;
    private String notifyUrl;
    private String returnUrl;
    private String receiveName;
    private Duration timeout = Duration.ofMinutes(30);

    public Builder baseUrl(String baseUrl)         { this.baseUrl = baseUrl;        return this; }
    public Builder appId(String appId)             { this.appId = appId;            return this; }
    public Builder appKey(String appKey)           { this.appKey = appKey;          return this; }
    public Builder shortCode(String shortCode)     { this.shortCode = shortCode;    return this; }
    public Builder publicKey(String publicKey)     { this.publicKey = publicKey;    return this; }
    public Builder notifyUrl(String notifyUrl)     { this.notifyUrl = notifyUrl;    return this; }
    public Builder returnUrl(String returnUrl)     { this.returnUrl = returnUrl;    return this; }
    public Builder receiveName(String receiveName) { this.receiveName = receiveName;return this; }
    public Builder timeout(Duration timeout)       { this.timeout = timeout;        return this; }

    public TelebirrProperties build() {
      return new TelebirrProperties(this);
    }
  }

  // Getters
  public String getBaseUrl()     { return baseUrl; }
  public String getAppId()       { return appId; }
  public String getAppKey()      { return appKey; }
  public String getShortCode()   { return shortCode; }
  public String getPublicKey()   { return publicKey; }
  public String getNotifyUrl()   { return notifyUrl; }
  public String getReturnUrl()   { return returnUrl; }
  public String getReceiveName() { return receiveName; }
  public Duration getTimeout()   { return timeout; }
}