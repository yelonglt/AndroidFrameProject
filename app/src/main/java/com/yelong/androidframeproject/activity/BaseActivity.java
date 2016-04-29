package com.yelong.androidframeproject.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.leakcanary.RefWatcher;
import com.yelong.androidframeproject.MainApplication;
import com.yelong.androidframeproject.R;
import com.yelong.androidframeproject.utils.DensityUtil;
import com.yelong.androidframeproject.view.ToolBarHelper;

/**
 * Created by eyetech on 16/4/17.
 */
public class BaseActivity extends AppCompatActivity {
    private ToolBarHelper mToolBarHelper;
    private Toolbar mToolbar;

    //左右图片按钮
    private ImageView mLeftImageView;
    private ImageView mRightImageView;
    //左右文本按钮
    private TextView mLeftTextView;
    private TextView mRightTextView;
    private TextView mCenterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 使用LeakCanary检测是否有内存泄露
        RefWatcher refWatcher = MainApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        mToolBarHelper = new ToolBarHelper(this, layoutResID);
        mToolbar = mToolBarHelper.getToolBar();
        setContentView(mToolBarHelper.getContentView());
        /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(mToolbar);
        /*自定义的一些操作*/
        onCreateCustomToolBar(mToolbar);

    }

    private void onCreateCustomToolBar(Toolbar mToolbar) {
        mLeftImageView = (ImageView) mToolbar.findViewById(R.id.toolbar_left_iv);
        mRightImageView = (ImageView) mToolbar.findViewById(R.id.toolbar_right_iv);
        mLeftTextView = (TextView) mToolbar.findViewById(R.id.toolbar_left_tv);
        mRightTextView = (TextView) mToolbar.findViewById(R.id.toolbar_right_tv);
        mCenterTextView = (TextView) mToolbar.findViewById(R.id.toolbar_title);
    }

    /**
     * 设置Toolbar的左边按钮
     *
     * @param resId    左边图片按钮，resId == 0 显示默认图片
     * @param listener 按钮监听器
     */
    public void setLeftButtonVisible(@DrawableRes int resId, View.OnClickListener listener) {
        mLeftImageView.setVisibility(View.VISIBLE);
        mLeftImageView.setImageResource(resId == 0 ? R.mipmap.back : resId);
        mLeftImageView.setOnClickListener(listener);
    }

    /**
     * 设置Toolbar的左边按钮
     *
     * @param title    左边文本，文本为空不显示
     * @param listener 按钮监听器
     */
    public void setLeftButtonVisible(String title, View.OnClickListener listener) {
        if (TextUtils.isEmpty(title)) {
            mLeftTextView.setVisibility(View.GONE);
        } else {
            mLeftTextView.setVisibility(View.VISIBLE);
            mLeftTextView.setText(title);
            mLeftTextView.setOnClickListener(listener);
        }
    }

    /**
     * 设置Toolbar的左边按钮
     *
     * @param title    左边文本，文本为空不显示
     * @param resId    文本左边的图片，resId == 0 显示默认图片
     * @param listener 按钮监听器
     */
    public void setLeftButtonVisible(String title, @DrawableRes int resId, View.OnClickListener listener) {
        if (TextUtils.isEmpty(title)) {
            mLeftTextView.setVisibility(View.GONE);
        } else {
            mLeftTextView.setVisibility(View.VISIBLE);
            mLeftTextView.setText(title);
            mLeftTextView.setOnClickListener(listener);
            Drawable drawable = getResources().getDrawable(resId == 0 ? R.mipmap.back : resId);
            assert drawable != null;
            drawable.setBounds(0, 0, DensityUtil.dp2px(this, 30), DensityUtil.dp2px(this, 30));
            mLeftTextView.setCompoundDrawables(drawable, null, null, null);
        }
    }

    /**
     * 设置Toolbar的标题栏
     *
     * @param title
     */
    public void setToolbarTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mCenterTextView.setVisibility(View.VISIBLE);
            mCenterTextView.setText(title);
        } else {
            mCenterTextView.setVisibility(View.GONE);
        }
    }


    /**
     * 设置Toolbar的标题栏
     *
     * @param title 标题栏文本，文本为空 不显示
     * @param resId 标题栏是否显示图片，不显示给0
     */
    public void setToolbarTitle(String title, @DrawableRes int resId) {
        if (!TextUtils.isEmpty(title)) {
            mCenterTextView.setVisibility(View.VISIBLE);
            mCenterTextView.setText(title);

            if (resId != 0) {
                Drawable drawable = getResources().getDrawable(resId);
                assert drawable != null;
                drawable.setBounds(0, 0, DensityUtil.dp2px(this, 30), DensityUtil.dp2px(this, 30));
                mCenterTextView.setCompoundDrawables(drawable, null, null, null);
            }
        } else {
            mCenterTextView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置Toolbar的右边按钮
     *
     * @param resId    右边图片按钮，resId == 0 不显示图片
     * @param listener 按钮监听器
     */
    public void setRightButtonVisible(@DrawableRes int resId, View.OnClickListener listener) {
        if (resId == 0) {
            mRightImageView.setVisibility(View.GONE);
        } else {
            mRightImageView.setVisibility(View.VISIBLE);
            mRightImageView.setImageResource(resId);
            mRightImageView.setOnClickListener(listener);
        }
    }

    /**
     * 设置Toolbar的右边按钮
     *
     * @param title    右边文本，文本为空不显示
     * @param listener 按钮监听器
     */
    public void setRightButtonVisible(String title, View.OnClickListener listener) {
        if (TextUtils.isEmpty(title)) {
            mRightTextView.setVisibility(View.GONE);
        } else {
            mRightTextView.setVisibility(View.VISIBLE);
            mRightTextView.setText(title);
            mRightTextView.setOnClickListener(listener);
        }
    }

    /**
     * 设置Toolbar的右边按钮
     *
     * @param title    右边文本，文本为空不显示
     * @param resId    文本左边的图片，resId == 0 不显示图片
     * @param listener 按钮监听器
     */
    public void setRightButtonVisible(String title, @DrawableRes int resId, View.OnClickListener listener) {
        if (TextUtils.isEmpty(title)) {
            mRightTextView.setVisibility(View.GONE);
        } else {
            mRightTextView.setVisibility(View.VISIBLE);
            mRightTextView.setText(title);
            mRightTextView.setOnClickListener(listener);
            if (resId != 0) {
                Drawable drawable = getResources().getDrawable(resId);
                assert drawable != null;
                drawable.setBounds(0, 0, DensityUtil.dp2px(this, 30), DensityUtil.dp2px(this, 30));
                mRightTextView.setCompoundDrawables(drawable, null, null, null);
            }
        }
    }

    /**
     * 设置Toolbar是否可见
     *
     * @param isVisible
     */
    public void setToolbarVisible(boolean isVisible) {
        if (isVisible) {
            mToolbar.setVisibility(View.VISIBLE);
        } else {
            mToolbar.setVisibility(View.GONE);
        }
    }

    /**
     * 通过Id找到View，不用显示的进行强制类型转化
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }
}
