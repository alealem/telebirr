package io.github.lealem.telebirr.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Business content for preorder request.
 */
public class BizContent {
    @JsonProperty("notify_url")
    private String notifyUrl;
    @JsonProperty("appid")
    private String appId;
    @JsonProperty("merch_code")
    private String merchCode;
    @JsonProperty("merch_order_id")
    private String merchOrderId;
    @JsonProperty("trade_type")
    private String tradeType;
    @JsonProperty("title")
    private String title;
    @JsonProperty("total_amount")
    private String totalAmount;
    @JsonProperty("trans_currency")
    private String transCurrency;
    @JsonProperty("timeout_express")
    private String timeoutExpress;
    @JsonProperty("business_type")
    private String businessType;
    @JsonProperty("payee_identifier")
    private String payeeIdentifier;
    @JsonProperty("payee_identifier_type")
    private String payeeIdentifierType;
    @JsonProperty("payee_type")
    private String payeeType;
    @JsonProperty("redirect_url")
    private String redirectUrl;
    @JsonProperty("callback_info")
    private String callbackInfo;

    public String getNotifyUrl() { return notifyUrl; }
    public void setNotifyUrl(String notifyUrl) { this.notifyUrl = notifyUrl; }
    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public String getMerchCode() { return merchCode; }
    public void setMerchCode(String merchCode) { this.merchCode = merchCode; }
    public String getMerchOrderId() { return merchOrderId; }
    public void setMerchOrderId(String merchOrderId) { this.merchOrderId = merchOrderId; }
    public String getTradeType() { return tradeType; }
    public void setTradeType(String tradeType) { this.tradeType = tradeType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTotalAmount() { return totalAmount; }
    public void setTotalAmount(String totalAmount) { this.totalAmount = totalAmount; }
    public String getTransCurrency() { return transCurrency; }
    public void setTransCurrency(String transCurrency) { this.transCurrency = transCurrency; }
    public String getTimeoutExpress() { return timeoutExpress; }
    public void setTimeoutExpress(String timeoutExpress) { this.timeoutExpress = timeoutExpress; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public String getPayeeIdentifier() { return payeeIdentifier; }
    public void setPayeeIdentifier(String payeeIdentifier) { this.payeeIdentifier = payeeIdentifier; }
    public String getPayeeIdentifierType() { return payeeIdentifierType; }
    public void setPayeeIdentifierType(String payeeIdentifierType) { this.payeeIdentifierType = payeeIdentifierType; }
    public String getPayeeType() { return payeeType; }
    public void setPayeeType(String payeeType) { this.payeeType = payeeType; }
    public String getRedirectUrl() { return redirectUrl; }
    public void setRedirectUrl(String redirectUrl) { this.redirectUrl = redirectUrl; }
    public String getCallbackInfo() { return callbackInfo; }
    public void setCallbackInfo(String callbackInfo) { this.callbackInfo = callbackInfo; }
}