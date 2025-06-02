package io.github.lealem.telebirr.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response for unified order (preorder).
 */
public class PreOrderResponse {
    @JsonProperty("biz_content")
    private BizContentResponse bizContent;
    @JsonProperty("sign")
    private String sign;
    @JsonProperty("sign_type")
    private String signType;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("nonce_str")
    private String nonceStr;
    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;

    public static class BizContentResponse {
        @JsonProperty("prepay_id")
        private String prepayId;

        public String getPrepayId() { return prepayId; }
        public void setPrepayId(String prepayId) { this.prepayId = prepayId; }
    }

    public BizContentResponse getBizContent() { return bizContent; }
    public void setBizContent(BizContentResponse bizContent) { this.bizContent = bizContent; }
    public String getSign() { return sign; }
    public void setSign(String sign) { this.sign = sign; }
    public String getSignType() { return signType; }
    public void setSignType(String signType) { this.signType = signType; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getNonceStr() { return nonceStr; }
    public void setNonceStr(String nonceStr) { this.nonceStr = nonceStr; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}