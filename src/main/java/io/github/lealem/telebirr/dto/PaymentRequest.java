package io.github.lealem.telebirr.dto;

public record PaymentRequest(
    String outTradeNo,
    String subject,
    String totalAmount
) {}