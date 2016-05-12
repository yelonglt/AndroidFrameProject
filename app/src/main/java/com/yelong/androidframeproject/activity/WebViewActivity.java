package com.yelong.androidframeproject.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yelong.androidframeproject.R;
import com.yelong.androidframeproject.web.WebJsInterface;

import java.util.ArrayList;

/**
 * Created by eyetech on 16/5/4.
 * mail:354734713@qq.com
 */
public class WebViewActivity extends BaseActivity {

    private WebView mWebView;

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    //适配Android5.0以上版本
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    private final static int FILECHOOSER_RESULTCODE_ANDROID5 = 2;

    //加载所有页面的url
    private ArrayList<String> loadHistoryUrls = new ArrayList<>();

    //初始化的页面
    private String defaultUrl = "http://www.baidu.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebView = (WebView) findViewById(R.id.src_web_view);
        initWebSetting();
        if (getIntent().getStringExtra("DefaultUrl") != null) {
            defaultUrl = getIntent().getStringExtra("DefaultUrl");
        }
        mWebView.loadUrl(defaultUrl);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //url是电话 就直接拨打
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //页面加载完成,完成图片的全部显示
                if (!mWebView.getSettings().getLoadsImagesAutomatically()) {
                    mWebView.getSettings().setLoadsImagesAutomatically(true);
                }

                //添加加载的url
                if (loadHistoryUrls.size() == 0) {
                    loadHistoryUrls.add(url);
                } else {
                    if (!url.equals(loadHistoryUrls.get(loadHistoryUrls.size() - 1))) {
                        loadHistoryUrls.add(url);
                    }
                }
                super.onPageFinished(view, url);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            // Android > 5.0 + 调用这个方法
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadMessageForAndroid5 = filePathCallback;
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_ANDROID5);

                return true;
            }

            // Android > 4.1.1 调用这个方法
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "完成操作需要使用"), FILECHOOSER_RESULTCODE);

            }

            // 3.0 + 调用这个方法
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "完成操作需要使用"), FILECHOOSER_RESULTCODE);
            }

            // Android < 3.0 调用这个方法
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "完成操作需要使用"), FILECHOOSER_RESULTCODE);

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //设置标题栏
                if (TextUtils.isEmpty(title)) {
                    setToolbarTitle(getResources().getString(R.string.app_name));
                } else {
                    setToolbarTitle(title);
                }
            }
        });

        mWebView.addJavascriptInterface(new WebJsInterface(), "webjs");
    }

    private void initWebSetting() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDefaultTextEncodingName("UTF-8");

        //设置存储
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        settings.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());

        //设置定位
        settings.setDatabaseEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath(getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath());

        //加快html装载速度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }

        //设置远程调试
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else if (requestCode == FILECHOOSER_RESULTCODE_ANDROID5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack() && loadHistoryUrls.size() > 0) {
            //view.goBack();
            boolean isRemove = loadHistoryUrls.remove(loadHistoryUrls.get(loadHistoryUrls.size() - 1));
            if (isRemove && loadHistoryUrls.size() > 0) {
                mWebView.loadUrl(loadHistoryUrls.get(loadHistoryUrls.size() - 1));
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadHistoryUrls.clear();
    }
}
