package com.yelong.androidframeproject.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.yelong.androidframeproject.R;

import java.util.List;

/**
 * 向上跑马灯效果,类似淘宝头条
 * Created by yelong on 16/7/23.
 * mail:354734713@qq.com
 */
public class UpMarqueeView extends ViewFlipper {
    private Context mContext;
    private int flipInterval = 2000;
    private int animDuration = 0;

    public UpMarqueeView(Context context) {
        this(context, null);
    }

    public UpMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        // 加载自定义的属性
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.UpMarqueeView);
        for (int i = 0; i < array.getIndexCount(); i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.UpMarqueeView_flipInterval:
                    flipInterval = array.getInt(attr, 2000);
                    break;
                case R.styleable.UpMarqueeView_animDuration:
                    animDuration = array.getInt(attr, 0);
                    break;
            }
        }
        array.recycle();

        setFlipInterval(flipInterval);
        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);
        if (animDuration != 0) animIn.setDuration(animDuration);
        setInAnimation(animIn);
        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);
        if (animDuration != 0) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    /**
     * 给View填充布局和数据
     *
     * @param data
     */
    public void setData(final List<String> data) {
        if (null == data || data.size() == 0) return;
        removeAllViews();
        for (int i = 0; i < data.size(); i = i + 2) {
            //设置滚动的单个布局
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
                    .inflate(R.layout.up_marquee_view, null);
            //初始化布局的控件
            RelativeLayout marqueeView1 = (RelativeLayout) moreView.findViewById(R.id.marquee_view1);
            RelativeLayout marqueeView2 = (RelativeLayout) moreView.findViewById(R.id.marquee_view2);
            TextView content1 = (TextView) moreView.findViewById(R.id.content1);
            TextView content2 = (TextView) moreView.findViewById(R.id.content2);

            //对控件赋值
            content1.setText(data.get(i));
            if (data.size() > i + 1) {
                //当数据的大小为奇数第二个item不要显示
                content2.setText(data.get(i + 1));
            } else {
                marqueeView2.setVisibility(View.GONE);
            }
            //设置点击事件
            final int position = i;
            marqueeView1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position, data.get(position));
                    }
                }
            });
            marqueeView2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position + 1, data.get(position + 1));
                    }
                }
            });

            addView(moreView);
        }
        startFlipping();
    }

    /**
     * 点击
     */
    private OnItemClickListener onItemClickListener;

    /**
     * 设置监听接口
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * item_view的接口
     */
    public interface OnItemClickListener {
        //void onItemClick(int position, View view);
        void onItemClick(int position, String item);
    }
}
