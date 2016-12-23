package com.yelong.ulibrary.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

/**
 * 向上滑动帮助类
 * Created by yelong on 2016/12/23.
 * mail:354734713@qq.com
 */

public class SlideUpHelper implements View.OnTouchListener, ValueAnimator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

    private View mView;
    private float touchableTop;
    private int autoSlideDuration = 1000;
    private SlideListener mSlideListener;

    private ValueAnimator mValueAnimator;
    private float slideAnimationTo;

    //视图开始位置坐标Y
    private float startPositionY;
    //视图的偏移量Y
    private float translationY;
    private float curPositionY;
    //视图的高度
    private float mHeight;
    private boolean canSlide = true;
    private boolean hiddenInit;

    public SlideUpHelper(final View view) {
        this.mView = view;
        this.touchableTop = view.getResources().getDisplayMetrics().density * 300;
        view.setOnTouchListener(this);
        view.setPivotY(0);
        createAnimation();
        hideInit();
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (hiddenInit) {
                    mHeight = view.getHeight();
                    hideInit();
                }
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    public boolean isVisible() {
        return mView.getVisibility() == View.VISIBLE;
    }

    public void setSlideListener(SlideListener slideListener) {
        mSlideListener = slideListener;
    }

    public int getAutoSlideDuration() {
        return autoSlideDuration;
    }

    public void setAutoSlideDuration(int autoSlideDuration) {
        this.autoSlideDuration = autoSlideDuration;
    }

    public float getTouchableTop() {
        return touchableTop;
    }

    public void setTouchableTop(float touchableTop) {
        this.touchableTop = touchableTop;
    }

    /**
     * 创建动画
     */
    private void createAnimation() {
        mValueAnimator = ValueAnimator.ofFloat();
        mValueAnimator.setDuration(autoSlideDuration);
        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        mValueAnimator.addListener(this);
        mValueAnimator.addUpdateListener(this);
    }

    public void animationIn() {
        this.slideAnimationTo = 0;
        mValueAnimator.setFloatValues(mHeight, slideAnimationTo);
        mValueAnimator.start();
    }

    public void animationOut() {
        this.slideAnimationTo = mView.getHeight();
        mValueAnimator.setFloatValues(mView.getTranslationY(), slideAnimationTo);
        mValueAnimator.start();
    }

    private boolean isAnimationRunning() {
        return mValueAnimator != null && mValueAnimator.isRunning();
    }

    /**
     * 隐藏初始化
     */
    private void hideInit() {
        if (mView.getHeight() > 0) {
            mView.setTranslationY(mHeight);
            mView.setVisibility(View.GONE);
            notifyVisibilityChanged(View.GONE);
        } else {
            hiddenInit = true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float touchArea = event.getRawY() - mView.getTop();
        if (isAnimationRunning()) return false;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                this.mHeight = mView.getHeight();
                startPositionY = event.getRawY();
                translationY = mView.getTranslationY();
                if (touchableTop < touchArea) canSlide = false;

                break;
            case MotionEvent.ACTION_MOVE:
                float distance = event.getRawY() - startPositionY;
                float newTranslationY = translationY + distance;
                float percent = newTranslationY * 100 / mHeight;
                if (newTranslationY > 0 && canSlide) {
                    notifyPercentChanged(percent);
                    mView.setTranslationY(newTranslationY);
                }
                if (event.getRawY() > curPositionY) curPositionY = event.getRawY();

                break;
            case MotionEvent.ACTION_UP:
                float slideAnimationFrom = mView.getTranslationY();
                boolean mustSlideUp = curPositionY > event.getRawY();
                boolean scrollableAreaConsumed = mView.getTranslationY() > mView.getHeight() / 5;
                if (scrollableAreaConsumed && !mustSlideUp) {
                    slideAnimationTo = mView.getHeight();
                } else {
                    slideAnimationTo = 0;
                }
                mValueAnimator.setFloatValues(slideAnimationFrom, slideAnimationTo);
                mValueAnimator.start();
                canSlide = true;
                curPositionY = 0;

                break;
        }

        return true;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        mView.setVisibility(View.VISIBLE);
        notifyVisibilityChanged(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (slideAnimationTo > 0) {
            mView.setVisibility(View.GONE);
            notifyVisibilityChanged(View.GONE);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float value = (float) animation.getAnimatedValue();
        mView.setTranslationY(value);
        float percent = (mView.getY() - mView.getTop()) * 100 / mHeight;
        notifyPercentChanged(percent);
    }

    private void notifyPercentChanged(float percent) {
        if (mSlideListener != null) mSlideListener.onSlide(percent);
    }

    private void notifyVisibilityChanged(int visibility) {
        if (mSlideListener != null) mSlideListener.onVisibilityChanged(visibility);
    }


    public interface SlideListener {

        void onSlide(float percent);

        void onVisibilityChanged(int visibility);

    }
}
