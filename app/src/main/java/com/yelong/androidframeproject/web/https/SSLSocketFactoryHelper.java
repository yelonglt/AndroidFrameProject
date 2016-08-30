package com.yelong.androidframeproject.web.https;

import android.content.Context;
import android.support.annotation.RawRes;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;

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
     *
     * @param context
     * @param id      证书id
     * @return
     */
    public static SSLSocketFactory newSslSocketFactory(Context context, @RawRes int id) {
        try {
            // Get an instance of the Bouncy Castle KeyStore format
            //KeyStore trusted = KeyStore.getInstance(KeyStore.getDefaultType());
            KeyStore trusted = KeyStore.getInstance("PKCS12", "BC");

            InputStream in = context.getResources().openRawResource(id);

            CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
            Certificate cer;
            try {
                cer = cerFactory.generateCertificate(in);
            } finally {
                in.close();
            }

            trusted.load(null, null);
            trusted.setCertificateEntry("ca", cer);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(trusted);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /**
     * 信任所有https请求
     *
     * @return
     */
    public static SSLSocketFactory defaultSSLSocketFactory() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }

}
