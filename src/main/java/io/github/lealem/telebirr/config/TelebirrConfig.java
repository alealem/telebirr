package io.github.lealem.telebirr.config;

/**
 * Configuration holder for TeleBirr SDK.
 */
public class TelebirrConfig {
    private final String baseUrl;           // e.g. https://developerportal.ethiotelebirr.et:38443/payment
    private final String webBaseUrl;        // e.g. https://developerportal.ethiotelebirr.et:38443/payment/web/paygate?
    private final String fabricAppId;       // X-APP-Key for fabric token
    private final String appSecret;         // secret to obtain fabric token
    private final String merchantAppId;     // appid for merchant
    private final String merchantCode;      // merchant code
    private final String notifyUrl;         // URL for payment notifications (must be whitelisted)
    private final String payeeIdentifierType; // e.g. "04"
    private final String payeeType;         // e.g. "5000"
    private final String redirectUrl;       // URL to which user is redirected after payment
    private final String callbackInfo;      // optional field
    private final String privateKeyPem;     // Merchant's RSA private key in PEM format
    private final String publicKeyPem;      // SP's RSA public key in PEM format (for verifying notifications)

    private TelebirrConfig(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.webBaseUrl = builder.webBaseUrl;
        this.fabricAppId = builder.fabricAppId;
        this.appSecret = builder.appSecret;
        this.merchantAppId = builder.merchantAppId;
        this.merchantCode = builder.merchantCode;
        this.notifyUrl = builder.notifyUrl;
        this.payeeIdentifierType = builder.payeeIdentifierType;
        this.payeeType = builder.payeeType;
        this.redirectUrl = builder.redirectUrl;
        this.callbackInfo = builder.callbackInfo;
        this.privateKeyPem = builder.privateKeyPem;
        this.publicKeyPem = builder.publicKeyPem;
    }

    public String getBaseUrl() { return baseUrl; }
    public String getWebBaseUrl() { return webBaseUrl; }
    public String getFabricAppId() { return fabricAppId; }
    public String getAppSecret() { return appSecret; }
    public String getMerchantAppId() { return merchantAppId; }
    public String getMerchantCode() { return merchantCode; }
    public String getNotifyUrl() { return notifyUrl; }
    public String getPayeeIdentifierType() { return payeeIdentifierType; }
    public String getPayeeType() { return payeeType; }
    public String getRedirectUrl() { return redirectUrl; }
    public String getCallbackInfo() { return callbackInfo; }
    public String getPrivateKeyPem() { return privateKeyPem; }
    public String getPublicKeyPem() { return publicKeyPem; }

    public static class Builder {
        private String baseUrl;
        private String webBaseUrl;
        private String fabricAppId;
        private String appSecret;
        private String merchantAppId;
        private String merchantCode;
        private String notifyUrl;
        private String payeeIdentifierType;
        private String payeeType;
        private String redirectUrl;
        private String callbackInfo;
        private String privateKeyPem;
        private String publicKeyPem;

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }
        public Builder setWebBaseUrl(String webBaseUrl) {
            this.webBaseUrl = webBaseUrl;
            return this;
        }
        public Builder setFabricAppId(String fabricAppId) {
            this.fabricAppId = fabricAppId;
            return this;
        }
        public Builder setAppSecret(String appSecret) {
            this.appSecret = appSecret;
            return this;
        }
        public Builder setMerchantAppId(String merchantAppId) {
            this.merchantAppId = merchantAppId;
            return this;
        }
        public Builder setMerchantCode(String merchantCode) {
            this.merchantCode = merchantCode;
            return this;
        }
        public Builder setNotifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
            return this;
        }
        public Builder setPayeeIdentifierType(String payeeIdentifierType) {
            this.payeeIdentifierType = payeeIdentifierType;
            return this;
        }
        public Builder setPayeeType(String payeeType) {
            this.payeeType = payeeType;
            return this;
        }
        public Builder setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
            return this;
        }
        public Builder setCallbackInfo(String callbackInfo) {
            this.callbackInfo = callbackInfo;
            return this;
        }
        public Builder setPrivateKeyPem(String privateKeyPem) {
            this.privateKeyPem = privateKeyPem;
            return this;
        }
        public Builder setPublicKeyPem(String publicKeyPem) {
            this.publicKeyPem = publicKeyPem;
            return this;
        }
        public TelebirrConfig build() {
            return new TelebirrConfig(this);
        }
    }
}