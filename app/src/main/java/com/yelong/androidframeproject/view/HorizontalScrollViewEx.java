package com.yelong.androidframeproject.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 采用外部拦截事件实现滑动的嵌套,解决滑动冲突.外部拦截符合事件的分发机制
 * <p/>
 * 注意点:
 * 1.在onInterceptTouchEvent方法中
 * 1)DOWN事件不能拦截,因为一旦拦截,后续的MOVE和UP事件都会被父容器处理,子元素就没有办法接收到事件.
 * 2)MOVE事件,可以根据事件规则决定是否拦截.拦截规则是水平滑动距离大于垂直滑动距离就拦截事件
 * 3)UP事件必须返回false,假如返回true导致子视图的点击事件不能触发
 * <p/>
 * 2.在onTouchEvent方法中,当事件被拦截会被调用
 * 1)MOVE事件,手指滑动视图会不停的滑动,故调用scrollBy不停的滑动
 * 2)UP事件,当手指抬起的时候,需要实现子视图的切换所以配合Scroller实现页面的滑动
 * <p/>
 * Created by yelong on 16/8/12.
 * mail:354734713@qq.com
 */
public class HorizontalScrollViewEx extends ViewGroup {
    public static final String TAG = HorizontalScrollViewEx.class.getSimpleName();

    // 分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;
    // 分别记录上次滑动的坐标(onInterceptTouchEvent)
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;

    private Scroller mScroller;
    //速度追踪类
    private VelocityTracker mVelocityTracker;

    //子视图的个数、子视图的宽度和当前显示的子视图
    private int mChildrenSize;
    private int mChildWidth;
    private int mChildIndex;

    public HorizontalScrollViewEx(Context context) {
        this(context, null);
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化相关的值
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth;
        int measuredHeight;
        final int childCount = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthSpaceSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpaceSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            final View childView = getChildAt(0);
            measuredHeight = childView.getMeasuredHeight();
            setMeasuredDimension(widthSpaceSize, measuredHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            final View childView = getChildAt(0);
            measuredWidth = childView.getMeasuredWidth() * childCount;
            setMeasuredDimension(measuredWidth, heightSpaceSize);
        } else {
            final View childView = getChildAt(0);
            measuredWidth = childView.getMeasuredWidth() * childCount;
            measuredHeight = childView.getMeasuredHeight();
            setMeasuredDimension(measuredWidth, measuredHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        final int childCount = getChildCount();
        mChildrenSize = childCount;

        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                final int childWidth = childView.getMeasuredWidth();
                mChildWidth = childWidth;
                childView.layout(childLeft, 0, childLeft + childWidth,
                        childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    //事件的处理
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                intercepted = false;
                if (!mScroller.isFinished()) {
                    //优化滑动体验加入的
                    mScroller.abortAnimation();
                    intercepted = true;
                }

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                intercepted = Math.abs(deltaX) > Math.abs(deltaY);

                break;
            }
            case MotionEvent.ACTION_UP: {
                intercepted = false;

                break;
            }
            default:
                break;
        }

        Log.d(TAG, "intercepted=" + intercepted);
        mLastX = x;
        mLastY = y;
        mLastXIntercept = x;
        mLastYIntercept = y;
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //添加滑动追踪
        mVelocityTracker.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    break;
                }
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                //int deltaY = y - mLastY;
                scrollBy(-deltaX, 0);
                break;
            }
            case MotionEvent.ACTION_UP: {
                int scrollX = getScrollX();
                //int scrollToChildIndex = scrollX / mChildWidth;
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();
                if (Math.abs(xVelocity) >= 50) {
                    mChildIndex = xVelocity > 0 ? mChildIndex - 1 : mChildIndex + 1;
                } else {
                    mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;
                }
                mChildIndex = Math.max(0, Math.min(mChildIndex, mChildrenSize - 1));
                int dx = mChildIndex * mChildWidth - scrollX;
                smoothScrollBy(dx, 0);
                mVelocityTracker.clear();
                break;
            }
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
        return true;
    }

    private void smoothScrollBy(int dx, int dy) {
        //500毫秒内滑动dx,效果是缓慢的滑动,这个滑动是content并非view
        mScroller.startScroll(getScrollX(), dy, dx, dy, 500);
        //调用invalidate方法会导致View的重绘,在View的draw方法又会去调computeScroll方法
        //computeScroll方法是空实现,因此需要我们自己实现
        invalidate();
    }

    @Override
    public void computeScroll() {
        //computeScrollOffset根据时间的流逝计算当前X和Y,返回true表示滑动还没有结束
        if (mScroller.computeScrollOffset()) {
            //Scroller对象获取当前X和Y,然后scrollTo实现滑动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //postInvalidate又会导致View的重绘,如此反复,直至结束,实现View的滑动
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
        super.onDetachedFromWindow();
    }
}
