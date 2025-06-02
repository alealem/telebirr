package io.github.lealem.telebirr;

import io.github.lealem.telebirr.config.TelebirrConfig;
import io.github.lealem.telebirr.exception.SignatureException;
import io.github.lealem.telebirr.exception.TelebirrException;
import io.github.lealem.telebirr.model.*;
import io.github.lealem.telebirr.util.HttpClientFactory;
import io.github.lealem.telebirr.util.JsonUtil;
import io.github.lealem.telebirr.util.SignatureUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Main TeleBirr SDK client.
 */
public class TelebirrClient {
    private final TelebirrConfig config;
    private final HttpClient httpClient;

    public TelebirrClient(TelebirrConfig config, boolean insecure) throws TelebirrException {
        this.config = config;
        try {
            this.httpClient = insecure ? HttpClientFactory.createInsecureClient()
                                       : HttpClientFactory.createDefaultClient();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new TelebirrException("Failed to initialize HTTP client", e);
        }
    }

    /**
     * Step 1: Apply fabric token.
     */
    public FabricTokenResponse getFabricToken() throws TelebirrException {
        try {
            String url = config.getBaseUrl() + "/v1/token";
            Map<String, String> bodyMap = new HashMap<>();
            bodyMap.put("appSecret", config.getAppSecret());
            String jsonBody = JsonUtil.toJson(bodyMap);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("X-APP-Key", config.getFabricAppId())
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new TelebirrException("Failed to get fabric token, HTTP " + response.statusCode());
            }
            return JsonUtil.fromJson(response.body(), FabricTokenResponse.class);
        } catch (IOException | InterruptedException e) {
            throw new TelebirrException("Error during fabric token request", e);
        }
    }

    /**
     * Step 2: Create a unified order (prepaid order) and return the PreOrderResponse.
     */
    public PreOrderResponse createOrder(String title, String amount) throws TelebirrException {
        FabricTokenResponse tokenResponse = getFabricToken();
        String fabricToken = tokenResponse.getToken();

        try {
            String url = config.getBaseUrl() + "/v1/merchant/preOrder";

            PreOrderRequest req = new PreOrderRequest();
            String timestamp = String.valueOf(Instant.now().getEpochSecond());
            String nonceStr = UUID.randomUUID().toString().replace("-", "");
            req.setTimestamp(timestamp);
            req.setNonceStr(nonceStr);
            req.setMethod("payment.preorder");
            req.setVersion("1.0");

            BizContent biz = new BizContent();
            biz.setNotifyUrl(config.getNotifyUrl());
            biz.setAppId(config.getMerchantAppId());
            biz.setMerchCode(config.getMerchantCode());
            biz.setMerchOrderId(String.valueOf(Instant.now().toEpochMilli()));
            biz.setTradeType("Checkout");
            biz.setTitle(title);
            biz.setTotalAmount(amount);
            biz.setTransCurrency("ETB");
            biz.setTimeoutExpress("120m");
            biz.setBusinessType("BuyGoods");
            biz.setPayeeIdentifier(config.getMerchantCode());
            biz.setPayeeIdentifierType(config.getPayeeIdentifierType());
            biz.setPayeeType(config.getPayeeType());
            biz.setRedirectUrl(config.getRedirectUrl());
            biz.setCallbackInfo(config.getCallbackInfo());
            req.setBizContent(biz);

            Map<String, String> signingMap = new HashMap<>();
            signingMap.put("timestamp", timestamp);
            signingMap.put("nonce_str", nonceStr);
            signingMap.put("method", req.getMethod());
            signingMap.put("version", req.getVersion());
            signingMap.putAll(flattenBizContent(biz));

            String signature = SignatureUtil.signRequest(signingMap, config.getPrivateKeyPem());
            req.setSign(signature);
            req.setSignType("SHA256WithRSA");

            String jsonBody = JsonUtil.toJson(req);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("X-APP-Key", config.getFabricAppId())
                    .header("Authorization", fabricToken)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() != 200) {
                throw new TelebirrException("Failed to create order, HTTP " + httpResponse.statusCode());
            }
            return JsonUtil.fromJson(httpResponse.body(), PreOrderResponse.class);
        } catch (IOException | InterruptedException | SignatureException e) {
            throw new TelebirrException("Error during createOrder", e);
        }
    }

    /**
     * Step 3 & 4: Build the web checkout URL given a prepayId.
     */
    public String buildWebPayUrl(String prepayId) throws TelebirrException {
        try {
            String timestamp = String.valueOf(Instant.now().getEpochSecond());
            String nonceStr = UUID.randomUUID().toString().replace("-", "");

            Map<String, String> map = new HashMap<>();
            map.put("appid", config.getMerchantAppId());
            map.put("merch_code", config.getMerchantCode());
            map.put("nonce_str", nonceStr);
            map.put("prepay_id", prepayId);
            map.put("timestamp", timestamp);

            String sign = SignatureUtil.signRequest(map, config.getPrivateKeyPem());

            String rawRequest = String.format("appid=%s&merch_code=%s&nonce_str=%s&prepay_id=%s&timestamp=%s&sign=%s&sign_type=SHA256WithRSA",
                    config.getMerchantAppId(),
                    config.getMerchantCode(),
                    nonceStr,
                    prepayId,
                    timestamp,
                    sign);

            return config.getWebBaseUrl() + rawRequest + "&version=1.0&trade_type=Checkout";
        } catch (SignatureException e) {
            throw new TelebirrException("Error building web pay URL", e);
        }
    }

    /**
     * Step 5: Verify payment notification signature.
     */
    public boolean verifyNotification(PaymentNotification notification) throws TelebirrException {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("notify_url", notification.getNotifyUrl());
            map.put("appid", notification.getAppId());
            map.put("notify_time", notification.getNotifyTime());
            map.put("merch_code", notification.getMerchCode());
            map.put("merch_order_id", notification.getMerchOrderId());
            map.put("payment_order_id", notification.getPaymentOrderId());
            map.put("total_amount", notification.getTotalAmount());
            map.put("trans_id", notification.getTransId());
            map.put("trans_currency", notification.getTransCurrency());
            map.put("trade_status", notification.getTradeStatus());
            map.put("trans_end_time", notification.getTransEndTime());

            return SignatureUtil.verifySignature(map, notification.getSign(), config.getPublicKeyPem());
        } catch (SignatureException e) {
            throw new TelebirrException("Notification signature verification failed", e);
        }
    }

    private Map<String, String> flattenBizContent(BizContent biz) {
        Map<String, String> map = new HashMap<>();
        map.put("notify_url", biz.getNotifyUrl());
        map.put("appid", biz.getAppId());
        map.put("merch_code", biz.getMerchCode());
        map.put("merch_order_id", biz.getMerchOrderId());
        map.put("trade_type", biz.getTradeType());
        map.put("title", biz.getTitle());
        map.put("total_amount", biz.getTotalAmount());
        map.put("trans_currency", biz.getTransCurrency());
        map.put("timeout_express", biz.getTimeoutExpress());
        map.put("business_type", biz.getBusinessType());
        map.put("payee_identifier", biz.getPayeeIdentifier());
        map.put("payee_identifier_type", biz.getPayeeIdentifierType());
        map.put("payee_type", biz.getPayeeType());
        map.put("redirect_url", biz.getRedirectUrl());
        map.put("callback_info", biz.getCallbackInfo());
        return map;
    }
}