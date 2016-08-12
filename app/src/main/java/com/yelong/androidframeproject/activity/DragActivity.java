package com.yelong.androidframeproject.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.yelong.androidframeproject.R;

/**
 * 可拖动视图
 * Created by yelong on 16/8/1.
 * mail:354734713@qq.com
 */
public class DragActivity extends BaseActivity {

    private TextView mDragView;
    //手指滑动速度追踪
    VelocityTracker mVelocityTracker;
    private int mMaxVelocity;

    private int mPointerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        setToolbarTitle("测试拖拽");

        mDragView = (TextView) findViewById(R.id.dragView);
        mMaxVelocity = ViewConfiguration.get(this).getScaledMaximumFlingVelocity();
        System.out.println("滑动最小距离 == " + ViewConfiguration.get(this).getScaledTouchSlop());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        printParams();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        acquireVelocityTracker(event);
        final VelocityTracker verTracker = mVelocityTracker;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //求第一个触点的id， 此时可能有多个触点，但至少一个
                mPointerId = event.getPointerId(0);
                break;

            case MotionEvent.ACTION_MOVE:
                //求伪瞬时速度
                verTracker.computeCurrentVelocity(1000, mMaxVelocity);
                final float velocityX = verTracker.getXVelocity(mPointerId);
                final float velocityY = verTracker.getYVelocity(mPointerId);
                System.out.println("velocityX == " + velocityX);
                System.out.println("velocityY == " + velocityY);
                break;

            case MotionEvent.ACTION_UP:
                releaseVelocityTracker();
                break;

            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                break;

            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void printParams() {
        System.out.println("left == " + mDragView.getLeft());
        System.out.println("right == " + mDragView.getRight());
        System.out.println("top == " + mDragView.getTop());
        System.out.println("bottom == " + mDragView.getBottom());

        System.out.println("x == " + mDragView.getX());
        System.out.println("y == " + mDragView.getY());
        System.out.println("translationX == " + mDragView.getTranslationX());
        System.out.println("translationY == " + mDragView.getTranslationY());
    }
}
