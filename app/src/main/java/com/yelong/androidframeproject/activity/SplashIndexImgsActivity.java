package com.yelong.androidframeproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yelong.androidframeproject.R;
import com.yelong.androidframeproject.model.IndicatorPair;
import com.yelong.androidframeproject.utils.DensityUtil;
import com.yelong.androidframeproject.utils.SplashImageUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 引导图页面
 * Created by eyetech on 16/4/26.
 */
public class SplashIndexImgsActivity extends Activity implements View.OnClickListener {
    // 默认图
    public static List<Integer> defImgs = new ArrayList<>();

    private ViewPager mSplashViewPager;
    private LayoutInflater mInflater;

    private LinearLayout mSplashPointLayout;

    private int lastPoint;
    private GestureDetector mDetector;
    // 手指在屏幕上滑动的宽度
    private int flaggingWidth;
    private int maxNumOfPixels;

    private DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_index_imgs);

        initDefImgs();

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        maxNumOfPixels = metrics.widthPixels * metrics.heightPixels;

        mInflater = LayoutInflater.from(this);
        flaggingWidth = metrics.widthPixels / 3;
        mDetector = new GestureDetector(new GuideViewTouch());
        findViewById(R.id.splash_skip).setOnClickListener(this);

        mSplashPointLayout = (LinearLayout) findViewById(R.id.splash_point_linear);

        SplashPagerAdapter adapter = new SplashPagerAdapter();
        mSplashViewPager = (ViewPager) findViewById(R.id.splash_viewpager);
        mSplashViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (lastPoint != position) {
                    mSplashPointLayout.getChildAt(lastPoint).setBackgroundResource(R.mipmap.splash_point_normal);
                    mSplashPointLayout.getChildAt(position).setBackgroundResource(R.mipmap.splash_point_focus);
                }
                lastPoint = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mSplashViewPager.setAdapter(adapter);

        List<IndicatorPair> indicators = new ArrayList<>();
        for (int i = 0; i < defImgs.size(); i++) {
            indicators.add(new IndicatorPair(defImgs.get(i), i, null));
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(DensityUtil.dp2px(this, 10), 0, DensityUtil.dp2px(this, 10), 0);

        if (indicators.size() > 0) {
            for (int i = 0; i < indicators.size(); i++) {
                ImageView pointView = new ImageView(this);

                if (i == 0) {
                    pointView.setBackgroundResource(R.mipmap.splash_point_focus);
                } else {
                    pointView.setBackgroundResource(R.mipmap.splash_point_normal);
                }
                mSplashPointLayout.addView(pointView, params);
            }
            adapter.setData(indicators);
            mSplashViewPager.setCurrentItem(0);
            lastPoint = 0;
        } else {
            onClick(null);
        }
    }

    /**
     * 初始化引导图
     */
    private void initDefImgs() {
        defImgs.clear();
        Class<?> resClzz = R.mipmap.class;
        Integer resId;
        for (int i = 0; i < 5; i++) {
            String id = "index_" + i;
            try {
                Field field = resClzz.getDeclaredField(id);
                resId = field.getInt(null);
                defImgs.add(resId > 0 ? resId : -1);
            } catch (Exception e) {
                System.out.println("初始化引导图失败");
            }
        }
    }

    private class GuideViewTouch extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (mSplashViewPager.getCurrentItem() == (mSplashViewPager.getAdapter().getCount() - 1)) {
                if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY())
                        && (e1.getX() - e2.getX() <= (-flaggingWidth) || e1.getX() - e2.getX() >= flaggingWidth)) {
                    if (e1.getX() - e2.getX() >= flaggingWidth) {
                        onClick(null);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        SplashIndexImgsActivity.this.finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mDetector.onTouchEvent(ev)) {
            ev.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * ViewPager的适配器
     *
     * @author yelong
     *         <p/>
     *         2015年8月26日
     */
    public class SplashPagerAdapter extends PagerAdapter {
        private List<IndicatorPair> indicators = new ArrayList<>();

        public void setData(List<IndicatorPair> indicators) {
            this.indicators = indicators;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return indicators.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View root = mInflater.inflate(R.layout.splash_imgs, container, false);
            IndicatorPair indicatorPair = indicators.get(position);

            ImageView imageView = (ImageView) root.findViewById(R.id.item_splash_img);
            if (indicatorPair.imgCache != null) {
                try {
                    imageView.setImageBitmap(
                            SplashImageUtil.getSmallBitmap(indicatorPair.imgCache.getAbsolutePath(), maxNumOfPixels));
                } catch (Exception e) {

                }
            }
            if (imageView.getDrawable() == null && indicatorPair.bg != -1) {// 默认图
                try {
                    imageView
                            .setImageBitmap(SplashImageUtil.getSmallBitmap(getResources(), indicatorPair.bg, maxNumOfPixels));
                    ;
                } catch (Exception e) {

                }
            }

            container.addView(root);
            return root;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View root = (View) object;
            ImageView imageView = (ImageView) root.findViewById(R.id.item_splash_img);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
            imageView.setImageBitmap(null);
            container.removeView(root);
        }
    }
}
