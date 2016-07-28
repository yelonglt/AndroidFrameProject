package com.dmall.mvp.pages.main;

import com.dmall.mvp.pages.BasePresenter;
import com.dmall.mvp.pages.BaseView;

/**
 * Created by yelong on 16/7/28.
 * mail:354734713@qq.com
 */
public interface MainContract {

    interface View extends BaseView<Presenter> {
        void showString(String content);
    }

    interface Presenter extends BasePresenter {

        void getString();
    }
}
