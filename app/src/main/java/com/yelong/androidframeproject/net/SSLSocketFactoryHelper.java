package com.yelong.androidframeproject.net;

import android.content.Context;
import android.support.annotation.RawRes;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 获取SSLSocketFactory帮助类
 * Created by yelong on 16/8/30.
 * mail:354734713@qq.com
 */
public class SSLSocketFactoryHelper {

    /**
     * Https信任指定的证书
     */
    public static SSLSocketFactory newSslSocketFactory(Context context, @RawRes int id) {
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().openRawResource(id);
            CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
            Certificate cer = cerFactory.generateCertificate(inputStream);

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", cer);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new IllegalStateException("SSLSocketFactoryHelper newSslSocketFactory Exception");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 信任所有https请求
     */
    public static SSLSocketFactory defaultSSLSocketFactory() {
        try {
            final TrustManager[] trustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            for (X509Certificate cert : chain) {
                                cert.checkValidity();
                            }
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            for (X509Certificate cert : chain) {
                                cert.checkValidity();
                            }
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagers, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return !TextUtils.isEmpty(hostname);
                }
            });

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("SSLSocketFactoryHelper defaultSSLSocketFactory Exception");
        }
    }

}
