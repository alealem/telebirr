package io.github.lealem.telebirr.dto;

public record NotificationResponse(
    String outTradeNo,
    String transactionNo,
    String tradeNo,
    String msisdn,
    String totalAmount,
    String tradeDate
) {}