package com.yelong.androidframeproject.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.yelong.androidframeproject.R;

/**
 * 滑动相关的界面
 * 知识点：
 * 1.滑动的是view的内容而不是本身
 * 2.滑动的距离getScrollX == view左边缘的X距离 - 滑动内容的左边缘的X距离（可以这么理解）
 * 3.也可以把当前滑动的view的左上角当做坐标原点。内容的滑动都是X方向的变化.
 * <p>
 * Created by eyetech on 16/6/22.
 * mail:354734713@qq.com
 */
public class ScrollerActivity extends BaseActivity {

    private RelativeLayout mainArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);
        setToolbarTitle("测试滑动");

        mainArea = (RelativeLayout) findViewById(R.id.main_area);
        Button scrollBy = (Button) findViewById(R.id.scrollby);
        Button scrollTo = (Button) findViewById(R.id.scrollto);

        scrollBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainArea.scrollBy(100, 100);
            }
        });

        scrollTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainArea.scrollTo(0, 0);
            }
        });
    }
}
