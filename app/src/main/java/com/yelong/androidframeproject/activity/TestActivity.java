package com.yelong.androidframeproject.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yelong.androidframeproject.R;

/**
 * 测试相关代码界面
 * Created by eyetech on 16/6/27.
 * mail:354734713@qq.com
 */
public class TestActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    public static final String TAG = "TestActivity";

    private ImageView bottomMask;
    private RelativeLayout drawerLayout;
    private TextView openDrawer;

    private Button mButton;

    private NestedScrollView nsv;

    private boolean isScroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setToolbarTitle("测试层级视图");


        bottomMask = (ImageView) findViewById(R.id.bottomMask);
        drawerLayout = (RelativeLayout) findViewById(R.id.drawerLayout);
        openDrawer = (TextView) findViewById(R.id.openDrawer);

        bottomMask.setOnClickListener(this);
        openDrawer.setOnClickListener(this);

        drawerLayout.setOnTouchListener(this);

        mButton = (Button) findViewById(R.id.test3);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "clicked button", Toast.LENGTH_SHORT).show();
            }
        });

        nsv = (NestedScrollView) findViewById(R.id.nsv);
        nsv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return isScroll;
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bottomMask:
                if (bottomMask.getAlpha() == 0.6f) {
                    openOrCloseDrawer();
                }
                break;

            case R.id.openDrawer:
                openOrCloseDrawer();

                break;


        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private boolean isOpen = false;

    private void openOrCloseDrawer() {
        isOpen = !isOpen;
        if (isOpen) {
            performAnimate(drawerLayout, drawerLayout.getHeight(), 500, 2000);
        } else {
            performAnimate(drawerLayout, drawerLayout.getHeight(), 40, 2000);
        }
    }


    private void performAnimate(final View target, final int start, final int end, long duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            //持有一个IntEvaluator对象，方便下面估值的时候使用
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //获得当前动画的进度值，整型，1-100之间
                int currentValue = (Integer) animator.getAnimatedValue();
                Log.d(TAG, "current value: " + currentValue);

                //计算当前进度占整个动画过程的比例，浮点型，0-1之间
                float fraction = currentValue / 100f;

                //直接调用整型估值器通过比例计算出宽度，然后给target设置高度
                target.getLayoutParams().height = mEvaluator.evaluate(fraction, start, end);
                target.requestLayout();
            }
        });

        //
        ObjectAnimator alphaAnimator;
        if (start < end) {
            alphaAnimator = ObjectAnimator.ofFloat(bottomMask, "alpha", 0, 0.6f);
        } else {
            alphaAnimator = ObjectAnimator.ofFloat(bottomMask, "alpha", 0.6f, 0f);
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(valueAnimator).with(alphaAnimator);
        animatorSet.setDuration(duration);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (start < end) {
                    bottomMask.setVisibility(View.VISIBLE);
                    //nsv.setVerticalScrollBarEnabled(true);
                    isScroll = true;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (start > end) {
                    bottomMask.setVisibility(View.GONE);
                    //nsv.setNestedScrollingEnabled(true);
                    isScroll = false;
                }
            }
        });

        animatorSet.start();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (v.getId() == R.id.drawerLayout) {
                //dispatchTouchEvent(event);
                return true;
            }
        }
        return false;
    }
}
