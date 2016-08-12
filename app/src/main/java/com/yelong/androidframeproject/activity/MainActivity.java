package com.yelong.androidframeproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yelong.androidframeproject.EventBusIndex;
import com.yelong.androidframeproject.R;
import com.yelong.androidframeproject.event.MessageEvent;
import com.yelong.androidframeproject.login.LoginInterceptor;
import com.yelong.androidframeproject.net.OkHttpClientManager;
import com.yelong.androidframeproject.view.UpMarqueeView;
import com.yelong.ulibrary.DrawableUtil;
import com.yelong.ulibrary.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class MainActivity extends BaseActivity {

    private TextView tvMessage;
    private ImageView mImageView;
    private UpMarqueeView mMarqueeView;

    //使用EventBus传递事件
    private EventBus mEventBus = EventBus.builder()
            .addIndex(new EventBusIndex()).installDefaultEventBus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbarTitle("主界面");
        mEventBus.register(this);

        tvMessage = (TextView) findViewById(R.id.message);

        setRightButtonVisible("测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gotoWebViewActivity();
                Intent intent = new Intent(MainActivity.this, EventConflictActivity.class);
                startActivity(intent);
            }
        });

        mImageView = (ImageView) findViewById(R.id.tintImage);
        mImageView.setImageDrawable(DrawableUtil.getStateDrawable(this, R.mipmap.ic_launcher, R.color.colorNormalAccent, R.color.colorAccent));

        testOkHttp();

        mMarqueeView = (UpMarqueeView) findViewById(R.id.today_headlines);
        mMarqueeView.setData(initData());
        mMarqueeView.setOnItemClickListener(new UpMarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String item) {
                ToastUtil.showShort(MainActivity.this, "你点击了==" + item);
            }
        });
    }

    public void testOkHttp() {
        OkHttpClientManager.getAsyn("https://api.github.com", new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                System.out.println("fail == " + e.toString());
            }

            @Override
            public void onResponse(String response) {
                System.out.println("success==" + response);
            }
        });
    }

    /**
     * 初始化数据
     */
    private List<String> initData() {
        List<String> data = new ArrayList<>();
        data.add("家人给2岁孩子喝这个，孩子智力倒退10岁!!!");
        data.add("iPhone8最感人变化成真，必须买买买买!!!!");
        data.add("简直是白菜价！日本玩家33万甩卖15万张游戏王卡");
        data.add("iPhone7价格曝光了！看完感觉我的腰子有点疼...");
        data.add("主人内疚逃命时没带够，回废墟狂挖30小时！");
//        data.add("竟不是小米乐视！看水抢了骁龙821首发了！！！");
        return data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //所有的View都不处理事件，事件最终会传递到这里
        return super.onTouchEvent(event);
    }

    /**
     * 跳转WebViewActivity
     */
    private void gotoWebViewActivity() {
        LoginInterceptor.interceptor(this, "activity.WebViewActivity", null);
    }
}
