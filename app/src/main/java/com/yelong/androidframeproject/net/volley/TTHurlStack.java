package com.yelong.androidframeproject.net.volley;

import com.android.volley.toolbox.HurlStack;
import com.yelong.androidframeproject.net.TrustAllCertsManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.SSLSocketFactory;

/**
 * Volley的HurlStack
 * Created by yelong on 2017/3/15.
 * mail:354734713@qq.com
 */
public class TTHurlStack extends HurlStack {

    public TTHurlStack(UrlRewriter urlRewriter, SSLSocketFactory sslSocketFactory) {
        super(urlRewriter, sslSocketFactory);
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        if (url.toString().contains("https")) {
            TrustAllCertsManager.allowAllSSL();
        }
        return super.createConnection(url);
    }
}
