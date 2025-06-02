package io.github.lealem.telebirr.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request payload for unified order (preorder).
 */
public class PreOrderRequest {
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("nonce_str")
    private String nonceStr;
    @JsonProperty("method")
    private String method;
    @JsonProperty("version")
    private String version;
    @JsonProperty("biz_content")
    private BizContent bizContent;
    @JsonProperty("sign")
    private String sign;
    @JsonProperty("sign_type")
    private String signType;

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getNonceStr() { return nonceStr; }
    public void setNonceStr(String nonceStr) { this.nonceStr = nonceStr; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public BizContent getBizContent() { return bizContent; }
    public void setBizContent(BizContent bizContent) { this.bizContent = bizContent; }
    public String getSign() { return sign; }
    public void setSign(String sign) { this.sign = sign; }
    public String getSignType() { return signType; }
    public void setSignType(String signType) { this.signType = signType; }
}