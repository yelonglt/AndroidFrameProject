package com.yelong.androidframeproject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yelong.androidframeproject.MainApplication;
import com.yelong.androidframeproject.R;
import com.yelong.androidframeproject.event.MessageEvent;
import com.yelong.androidframeproject.login.LoginInterceptor;
import com.yelong.androidframeproject.net.OkHttpClientManager;
import com.yelong.androidframeproject.view.FontButton;
import com.yelong.androidframeproject.view.ShadowCircleImageView;
import com.yelong.androidframeproject.view.UpMarqueeView;
import com.yelong.androidframeproject.view.WaveHorizontalView;
import com.yelong.ulibrary.DToast;
import com.yelong.ulibrary.DrawableUtil;
import com.yelong.ulibrary.SpannableStringUtil;
import com.yelong.ulibrary.ToastUtil;
import com.yelong.ulibrary.animation.Rotate3DAnimation;
import com.yelong.ulibrary.view.BadgeFactory;
import com.yelong.ulibrary.view.BadgeView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class MainActivity extends BaseActivity {

    private TextView tvMessage;
    private ImageView mImageView;
    private UpMarqueeView mMarqueeView;
    private Button mCenterButton;

    private WaveHorizontalView mWaveHorizontalView;
    private ImageView mCircleImageView;

    private FontButton mFontButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbarTitle("主界面");
        MainApplication.getEventBus().register(this);

        tvMessage = (TextView) findViewById(R.id.message);
        tvMessage.setText(SpannableStringUtil
                .getBuilder("Hello", this)
                .setBackgroundColor(getResources().getColor(R.color.colorAccent))
                .create());
        mCenterButton = (Button) findViewById(R.id.center_btn);

        setRightButtonVisible("测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gotoWebViewActivity();
                Intent intent = new Intent(MainActivity.this, MineActivity.class);
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

        BadgeFactory.create(this).setTextColor(Color.WHITE)
                .setWidthAndHeight(25, 25)
                .setBadgeBackground(Color.RED)
                .setTextSize(10)
                .setBadgeGravity(Gravity.RIGHT | Gravity.TOP)
                .setBadgeCount(20)
                .setShape(BadgeView.SHAPE_ROUND_RECTANGLE)
                .setMargin(0, 0, 5, 0).bind(mCenterButton);

        initWave();

        mFontButton = (FontButton) findViewById(R.id.font_button);
        assert mFontButton != null;
        mFontButton.setText("登   录");
        mFontButton.setBackground(R.color.colorPrimary, R.color.colorPrimaryDark);
        mFontButton.setTextColor(android.R.color.holo_blue_bright, android.R.color.holo_blue_dark);
        mFontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DToast.makeText(MainActivity.this, "你点击了").show();
            }
        });

        startAnimation();
    }

    private void initWave() {
        mWaveHorizontalView = (WaveHorizontalView) findViewById(R.id.mWaveHorizontalView);
        mCircleImageView = (ImageView) findViewById(R.id.mCircleImageView);

        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;
        mWaveHorizontalView.setWaveAnimationListener(new WaveHorizontalView.OnWaveAnimationListener() {
            @Override
            public void OnWaveAnimation(float y) {
                params.setMargins(0, 0, 0, (int) y + 2);
                mCircleImageView.setLayoutParams(params);
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

    private void startAnimation() {

        final ShadowCircleImageView imageView = (ShadowCircleImageView) findViewById(R.id.shadow_circle_view);
        imageView.post(new Runnable() {
            @Override
            public void run() {
                Rotate3DAnimation animation = new Rotate3DAnimation(0f, 360f,
                        imageView.getWidth() / 2, imageView.getHeight() / 2, 0,
                        Rotate3DAnimation.DIRECTION.Y, false);
                animation.setDuration(5000);
                imageView.setAnimation(animation);
                animation.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("MainActivity onDestroy");
        MainApplication.getEventBus().unregister(this);
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
