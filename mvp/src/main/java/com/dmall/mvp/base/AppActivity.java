package com.dmall.mvp.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dmall.mvp.R;

import java.lang.reflect.Method;

/**
 * 直接让App所有的Activity来实现
 * Created by yelong on 16/7/29.
 * mail:354734713@qq.com
 */
public abstract class AppActivity extends BaseActivity {

    /**
     * 获取Activity添加的第一个BaseFragment
     *
     * @return
     */
    protected abstract BaseFragment getFirstFragment();

    /**
     * 处理接收到的Intent参数
     *
     * @param intent
     */
    protected void handleIntent(Intent intent) {

    }

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        if (null != getIntent()) {
            handleIntent(getIntent());
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //避免重复添加Fragment
        if (null == getSupportFragmentManager().getFragments()) {
            BaseFragment firstFragment = getFirstFragment();
            if (null != firstFragment) {
                addFragment(firstFragment);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item0) {
            Toast.makeText(AppActivity.this, "nihao", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示item中的图片；
     *
     * @param view
     * @param menu
     * @return
     */
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_base;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fragment_container;
    }
}
