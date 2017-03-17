package com.yelong.androidframeproject.net;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 验证自签名证书
 * Created by yelong on 2017/3/15.
 * mail:354734713@qq.com
 */
public class SelfSignCertManager implements X509TrustManager {

    private X509Certificate mCertificate;

    public SelfSignCertManager(X509Certificate certificate) {
        mCertificate = certificate;
    }

    public SelfSignCertManager(String certificate) {
        mCertificate = readCert(certificate);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // 我们在客户端只做服务器端证书校验。
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // 确认服务器端证书的Intermediate CRT和代码中硬编码的CRT证书主体一致。
        if (!chain[0].getIssuerDN().equals(mCertificate.getSubjectDN())) {
            throw new CertificateException("Parent certificate of server was different than expected signing certificate");
        }

        try {
            // 确认服务器端证书被代码中 hard code 的 Intermediate CRT 证书的公钥签名。
            chain[0].verify(mCertificate.getPublicKey());
            // 确认服务器端证书没有过期
            chain[0].checkValidity();
        } catch (Exception e) {
            throw new CertificateException("Parent certificate of server was different than expected signing certificate");
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    private static X509Certificate readCert(String certificate) {
        if (TextUtils.isEmpty(certificate)) return null;
        InputStream inputStream = new ByteArrayInputStream(certificate.getBytes());
        X509Certificate cert = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate) cf.generateCertificate(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return cert;
    }
}
