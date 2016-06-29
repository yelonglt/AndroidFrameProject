package com.yelong.androidframeproject.web;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.yelong.androidframeproject.R;

/**
 * 带进度条的WebView
 * Created by eyetech on 16/6/27.
 * mail:354734713@qq.com
 */
public class ProgressWebView extends WebView {

    private ProgressBar mProgressBar;

    public ProgressWebView(Context context) {
        super(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 6);
        mProgressBar.setLayoutParams(layoutParams);

        Drawable drawable = context.getResources().getDrawable(R.drawable.web_progress_bar_states);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);

        setWebChromeClient(new WebClient());
    }

    public class WebClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE) mProgressBar.setVisibility(VISIBLE);
                mProgressBar.setProgress(newProgress);
            }

            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams layoutParams = (LayoutParams) mProgressBar.getLayoutParams();
        layoutParams.x = l;
        layoutParams.y = t;
        mProgressBar.setLayoutParams(layoutParams);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
