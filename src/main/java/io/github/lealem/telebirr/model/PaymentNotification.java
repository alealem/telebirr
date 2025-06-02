package io.github.lealem.telebirr.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model for payment notification callback payload.
 */
public class PaymentNotification {
    @JsonProperty("notify_url")
    private String notifyUrl;
    @JsonProperty("appid")
    private String appId;
    @JsonProperty("notify_time")
    private String notifyTime;
    @JsonProperty("merch_code")
    private String merchCode;
    @JsonProperty("merch_order_id")
    private String merchOrderId;
    @JsonProperty("payment_order_id")
    private String paymentOrderId;
    @JsonProperty("total_amount")
    private String totalAmount;
    @JsonProperty("trans_id")
    private String transId;
    @JsonProperty("trans_currency")
    private String transCurrency;
    @JsonProperty("trade_status")
    private String tradeStatus;
    @JsonProperty("trans_end_time")
    private String transEndTime;
    @JsonProperty("sign")
    private String sign;
    @JsonProperty("sign_type")
    private String signType;

    public String getNotifyUrl() { return notifyUrl; }
    public void setNotifyUrl(String notifyUrl) { this.notifyUrl = notifyUrl; }
    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public String getNotifyTime() { return notifyTime; }
    public void setNotifyTime(String notifyTime) { this.notifyTime = notifyTime; }
    public String getMerchCode() { return merchCode; }
    public void setMerchCode(String merchCode) { this.merchCode = merchCode; }
    public String getMerchOrderId() { return merchOrderId; }
    public void setMerchOrderId(String merchOrderId) { this.merchOrderId = merchOrderId; }
    public String getPaymentOrderId() { return paymentOrderId; }
    public void setPaymentOrderId(String paymentOrderId) { this.paymentOrderId = paymentOrderId; }
    public String getTotalAmount() { return totalAmount; }
    public void setTotalAmount(String totalAmount) { this.totalAmount = totalAmount; }
    public String getTransId() { return transId; }
    public void setTransId(String transId) { this.transId = transId; }
    public String getTransCurrency() { return transCurrency; }
    public void setTransCurrency(String transCurrency) { this.transCurrency = transCurrency; }
    public String getTradeStatus() { return tradeStatus; }
    public void setTradeStatus(String tradeStatus) { this.tradeStatus = tradeStatus; }
    public String getTransEndTime() { return transEndTime; }
    public void setTransEndTime(String transEndTime) { this.transEndTime = transEndTime; }
    public String getSign() { return sign; }
    public void setSign(String sign) { this.sign = sign; }
    public String getSignType() { return signType; }
    public void setSignType(String signType) { this.signType = signType; }
}