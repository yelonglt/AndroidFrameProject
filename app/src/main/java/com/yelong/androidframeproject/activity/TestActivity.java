package com.yelong.androidframeproject.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yelong.androidframeproject.R;
import com.yelong.androidframeproject.web.ProgressWebView;

/**
 * 测试相关代码界面
 * Created by eyetech on 16/6/27.
 * mail:354734713@qq.com
 */
public class TestActivity extends BaseActivity {

    private ProgressWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setToolbarTitle("测试加载进度条");

        mWebView = (ProgressWebView) findViewById(R.id.test_web_view);
        mWebView.loadUrl("http://www.sina.com.cn");

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
}
