package com.yelong.androidframeproject.net;

import android.content.Context;
import android.os.Environment;

import com.yelong.androidframeproject.net.cookie.CookiesManager;
import com.yelong.androidframeproject.net.interceptor.CacheInterceptor;
import com.yelong.androidframeproject.net.interceptor.LoggerInterceptor;
import com.yelong.androidframeproject.net.interceptor.ProgressInterceptor;

import java.io.File;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

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

    public OkHttpClientConfig addNetworkProgress() {
        mBuilder.addInterceptor(new ProgressInterceptor());
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
            keyStore.load(null, null);

            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                if (certificate != null) {
                    certificate.close();
                }
            }

            final X509TrustManager trustManager = new X509TrustManager() {
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
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            mBuilder.sslSocketFactory(sslSocketFactory, trustManager);
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

    /**
     * @return 生成OkHttpClient.Builder 对象
     */
    public OkHttpClient.Builder builder() {
        return mBuilder;
    }
}
