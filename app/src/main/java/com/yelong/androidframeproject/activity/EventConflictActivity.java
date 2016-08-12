package com.yelong.androidframeproject.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yelong.androidframeproject.R;
import com.yelong.androidframeproject.view.HorizontalScrollViewEx;

import java.util.ArrayList;

/**
 * Android处理事件冲突有两种方式
 * 1.事件外部拦截法,也是符合Android事件规则
 * 2.事件内部拦截法
 * Created by yelong on 16/8/12.
 * mail:354734713@qq.com
 */
public class EventConflictActivity extends BaseActivity {

    private HorizontalScrollViewEx mListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_conflict);
        setToolbarTitle("事件冲突处理页面");

        initView();
    }

    private void initView() {
        LayoutInflater inflater = getLayoutInflater();
        mListContainer = (HorizontalScrollViewEx) findViewById(R.id.container);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        for (int i = 0; i < 3; i++) {
            ViewGroup layout = (ViewGroup) inflater.inflate(
                    R.layout.event_child, mListContainer, false);
            layout.getLayoutParams().width = dm.widthPixels;
            TextView textView = (TextView) layout.findViewById(R.id.title);
            textView.setText("page " + (i + 1));
            layout.setBackgroundColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 0));
            createList(layout);
            mListContainer.addView(layout);
        }
    }

    private void createList(ViewGroup layout) {
        ListView listView = (ListView) layout.findViewById(R.id.list);
        ArrayList<String> data = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            data.add("name " + i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.event_child_list_item, R.id.name, data);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(EventConflictActivity.this, "click item",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
