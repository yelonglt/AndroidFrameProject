package com.yelong.androidframeproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yelong.androidframeproject.R;
import com.yelong.androidframeproject.event.MessageEvent;
import com.yelong.ulibrary.DrawableUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity {

    private TextView tvMessage;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbarTitle("主界面");

        tvMessage = (TextView) findViewById(R.id.message);

        setRightButtonVisible("测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });

        mImageView = (ImageView) findViewById(R.id.tintImage);
        mImageView.setImageDrawable(DrawableUtil.getStateDrawable(this, R.mipmap.ic_launcher, R.color.colorNormalAccent, R.color.colorAccent));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //EventBus.getDefault().post(new MessageEvent("你在干嘛呢？"));
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onPostEvent(MessageEvent event) {
        System.out.println("onPostEvent == " + event.getMessage());
        tvMessage.setText(event.getMessage());
        //默认方式，在发送线程中执行，即post事件在主线程中发送就到主线程中执行
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true, priority = 1)
    public void onMainEvent(MessageEvent event) {
        System.out.println("onMainEvent == " + event.getMessage());
        //tvMessage.setText(event.getMessage());
        //在UI主线程中执行
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onBackEvent(MessageEvent event) {
        System.out.println("onBackEvent == " + event.getMessage());
        //tvMessage.setText(event.getMessage());
        //在后台线程中执行
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onAsyncEvent(MessageEvent event) {
        System.out.println("onAsyncEvent == " + event.getMessage());
        //tvMessage.setText(event.getMessage());
        //强制在后台线程中执行
    }
}
