package io.github.lealem.telebirr.dto;

public record NotificationRequest(
    String data,
    String sign
) {}