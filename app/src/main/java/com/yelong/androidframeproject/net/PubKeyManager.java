package com.yelong.androidframeproject.net;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by yelong on 2017/3/15.
 * mail:354734713@qq.com
 */
public class PubKeyManager implements X509TrustManager {

    private String publicKey;

    public PubKeyManager(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (chain == null) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate array is null");
        }
        if (!(chain.length > 0)) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
        }

        TrustManagerFactory tmf;
        try {
            tmf = TrustManagerFactory.getInstance("X509");
            tmf.init((KeyStore) null);

            for (TrustManager trustManager : tmf.getTrustManagers()) {
                ((X509TrustManager) trustManager).checkServerTrusted(
                        chain, authType);
            }

        } catch (Exception e) {
            throw new CertificateException(e.toString());
        }

        RSAPublicKey rsaPublicKey = (RSAPublicKey) chain[0].getPublicKey();
        String encoded = new BigInteger(1, rsaPublicKey.getEncoded()).toString(16);

        final boolean expected = publicKey.equalsIgnoreCase(encoded);
        // fail if expected public key is different from our public key
        if (!expected) {
            throw new CertificateException("Not trusted");
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
