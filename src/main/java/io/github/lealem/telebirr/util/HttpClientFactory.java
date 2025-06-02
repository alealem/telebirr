package io.github.lealem.telebirr.util;

import javax.net.ssl.*;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

/**
 * Factory to create HTTP clients. By default, creates a client that trusts all certificates (for development).
 * In production, replace with a properly configured SSL context.
 */
public class HttpClientFactory {

    /**
     * Creates an HttpClient that disables SSL certificate verification.
     * WARNING: Use only for testing or in environments where certificate chains cannot be validated.
     */
    public static HttpClient createInsecureClient() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[0]; }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        SSLParameters sslParams = sslContext.getDefaultSSLParameters();

        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .sslContext(sslContext)
                .sslParameters(sslParams)
                .build();
    }

    /**
     * Creates a default HttpClient with standard SSL verification.
     */
    public static HttpClient createDefaultClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
    }
}