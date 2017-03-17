package com.yelong.androidframeproject.net;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.RawRes;

import com.yelong.androidframeproject.net.cookie.CookiesManager;
import com.yelong.androidframeproject.net.interceptor.CacheInterceptor;
import com.yelong.androidframeproject.net.interceptor.LoggerInterceptor;

import java.io.File;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;

/**
 * OkHttp配置管理类
 * Created by yelong on 2016/11/25.
 * mail:354734713@qq.com
 */

public class OkHttpClientConfig {

    private OkHttpClient.Builder mBuilder;

    private static class OkHttpConfigHolder {
        private static OkHttpClientConfig instance = new OkHttpClientConfig();
    }

    private OkHttpClientConfig() {
        mBuilder = new OkHttpClient.Builder();
    }

    public static OkHttpClientConfig getInstance() {
        return OkHttpConfigHolder.instance;
    }

    /**
     * 添加Cookie
     *
     * @return OkHttpClientConfig
     */
    public OkHttpClientConfig addCookie() {
        mBuilder.cookieJar(new CookiesManager());
        return this;
    }

    /**
     * 添加缓存
     *
     * @param context   上下文
     * @param cacheSize 缓存大小
     * @return OkHttpClientConfig
     */
    public OkHttpClientConfig addCache(Context context, int cacheSize) {
        File cacheDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheDir = context.getExternalCacheDir();
        } else {
            cacheDir = context.getCacheDir();
        }

        Cache cache = new Cache(cacheDir, cacheSize);
        mBuilder.cache(cache);
        mBuilder.addInterceptor(new CacheInterceptor());
        return this;
    }

    /**
     * 添加Logger
     *
     * @param tag logger标签信息
     * @return OkHttpClientConfig
     */
    public OkHttpClientConfig addLogger(String tag) {
        mBuilder.addInterceptor(new LoggerInterceptor(tag, true));
        return this;
    }


    /**
     * 添加https信任证书
     *
     * @param certificates 证书数组
     * @return OkHttpClientConfig
     */
    public OkHttpClientConfig addCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
            keyStore.load(null, null);

            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                if (certificate != null) {
                    certificate.close();
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            mBuilder.sslSocketFactory(sslSocketFactory, Platform.get().trustManager(sslSocketFactory));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public SSLSocketFactory newSslSocketFactory(String publicKey) {
        TrustManager tm[] = {new PubKeyManager(publicKey)};
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tm, null);
            sslSocketFactory = context.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }

    private SSLSocketFactory newSslSocketFactory(Context ctx,@RawRes int id) {
        try {
            KeyStore trusted = KeyStore.getInstance("PKCS12", "BC");

            InputStream in = ctx.getResources().openRawResource(id);

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

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            return context.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /**
     * @return 生成OkHttpClient.Builder 对象
     */
    public OkHttpClient.Builder builder() {
        return mBuilder;
    }
}
