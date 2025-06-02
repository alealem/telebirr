package io.github.lealem.telebirr.util;

import io.github.lealem.telebirr.exception.SignatureException;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Utility for generating and verifying RSA SHA256 signatures.
 */
public class SignatureUtil {

    /**
     * Sign the given parameters map with the provided private key (PEM). Returns Base64-encoded signature.
     */
    public static String signRequest(Map<String, String> params, String privateKeyPem) throws SignatureException {
        try {
            TreeMap<String, String> sorted = new TreeMap<>(params);
            String dataToSign = sorted.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&"));

            PrivateKey privateKey = loadPrivateKey(privateKeyPem);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(dataToSign.getBytes(StandardCharsets.UTF_8));
            byte[] signed = signature.sign();
            return Base64.getEncoder().encodeToString(signed);
        } catch (Exception e) {
            throw new SignatureException("Failed to sign request", e);
        }
    }

    /**
     * Verify the signature for given parameters map using provided public key PEM.
     */
    public static boolean verifySignature(Map<String, String> params, String signatureBase64, String publicKeyPem) throws SignatureException {
        try {
            TreeMap<String, String> sorted = new TreeMap<>(params);
            String data = sorted.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&"));

            PublicKey publicKey = loadPublicKey(publicKeyPem);
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] decodedSig = Base64.getDecoder().decode(signatureBase64);
            return sig.verify(decodedSig);
        } catch (Exception e) {
            throw new SignatureException("Failed to verify signature", e);
        }
    }

    private static PrivateKey loadPrivateKey(String pem) throws IOException, GeneralSecurityException {
        PemReader pemReader = new PemReader(new StringReader(pem));
        PemObject pemObject = pemReader.readPemObject();
        byte[] content = pemObject.getContent();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(content);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private static PublicKey loadPublicKey(String pem) throws IOException, GeneralSecurityException {
        PemReader pemReader = new PemReader(new StringReader(pem));
        PemObject pemObject = pemReader.readPemObject();
        byte[] content = pemObject.getContent();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(content);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}