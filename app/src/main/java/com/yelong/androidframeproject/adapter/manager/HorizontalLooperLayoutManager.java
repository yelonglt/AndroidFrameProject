package com.yelong.androidframeproject.adapter.manager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 水平循环布局管理器
 * Created by yelong on 2019-06-26.
 * mail:354734713@qq.com
 */
public class HorizontalLooperLayoutManager extends RecyclerView.LayoutManager {

    private boolean looperEnable;

    public void setLooperEnable(boolean looperEnable) {
        this.looperEnable = looperEnable;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0) {
            return;
        }
        //preLayout主要支持动画，直接跳过
        if (state.isPreLayout()) {
            return;
        }
        //将视图分离放入scrap缓存中，以准备重新对view进行排版
        detachAndScrapAttachedViews(recycler);

        int contentWidth = 0;
        for (int i = 0; i < getItemCount(); i++) {
            //初始化，将在屏幕内的view填充
            View itemView = recycler.getViewForPosition(i);
            addView(itemView);
            //测量itemView的宽高
            measureChildWithMargins(itemView, 0, 0);
            int width = getDecoratedMeasuredWidth(itemView);
            int height = getDecoratedMeasuredHeight(itemView);
            //根据itemView的宽高进行布局
            layoutDecorated(itemView, contentWidth, 0, contentWidth + width, height);

            contentWidth += width;
            //如果当前布局过的itemView的宽度总和大于RecyclerView的宽，则不再进行布局
            if (contentWidth > getWidth()) {
                break;
            }
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 1.左右滑动的时候，填充ChildView
        int scrollDx = fillChildView(dx, recycler);
        if (scrollDx == 0) return 0;
        // 2.滚动
        offsetChildrenHorizontal(-scrollDx);
        // 3.回收已经离开界面的
        recyclerHideView(dx, recycler);
        return scrollDx;
    }

    private int fillChildView(int dx, RecyclerView.Recycler recycler) {
        if (dx > 0) {
            //1.向左滚动
            View lastView = getChildAt(getChildCount() - 1);
            if (lastView == null) {
                return 0;
            }
            int lastPosition = getPosition(lastView);
            //2.可见的最后一个itemView完全滑进来了，需要补充新的
            if (lastView.getRight() < getWidth()) {
                View itemView = null;
                //3.判断可见的最后一个itemView的索引，
                // 如果是最后一个，则将下一个itemView设置为第一个，否则设置为当前索引的下一个
                if (lastPosition == getItemCount() - 1) {
                    if (looperEnable) {
                        itemView = recycler.getViewForPosition(0);
                    } else {
                        dx = 0;
                    }
                } else {
                    itemView = recycler.getViewForPosition(lastPosition + 1);
                }
                if (itemView == null) {
                    return 0;
                }
                //4.将新的itemView add进来并对其测量和布局
                addView(itemView);
                measureChildWithMargins(itemView, 0, 0);
                int width = getDecoratedMeasuredWidth(itemView);
                int height = getDecoratedMeasuredHeight(itemView);
                layoutDecorated(itemView, lastView.getRight(), 0, lastView.getRight() + width, height);
                return dx;
            }
        } else {
            //向右滚动
            View firstView = getChildAt(0);
            if (firstView == null) {
                return 0;
            }
            int firstPosition = getPosition(firstView);
            if (firstView.getLeft() >= 0) {
                View itemView = null;
                if (firstPosition == 0) {
                    if (looperEnable) {
                        itemView = recycler.getViewForPosition(getItemCount() - 1);
                    } else {
                        dx = 0;
                    }
                } else {
                    itemView = recycler.getViewForPosition(firstPosition - 1);
                }
                if (itemView == null) {
                    return 0;
                }
                addView(itemView, 0);
                measureChildWithMargins(itemView, 0, 0);
                int width = getDecoratedMeasuredWidth(itemView);
                int height = getDecoratedMeasuredHeight(itemView);
                layoutDecorated(itemView, firstView.getLeft() - width, 0, firstView.getLeft(), height);
            }
        }
        return dx;
    }

    private void recyclerHideView(int dx, RecyclerView.Recycler recycler) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view == null) {
                continue;
            }
            if (dx > 0) {
                //向左滚动，移除一个左边不在内容里的view
                if (view.getRight() < 0) {
                    removeAndRecycleView(view, recycler);
                }
            } else {
                //向右滚动，移除一个右边不在内容里的view
                if (view.getLeft() > getWidth()) {
                    removeAndRecycleView(view, recycler);
                }
            }
        }

    }

}
