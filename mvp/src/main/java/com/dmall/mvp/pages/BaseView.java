package com.dmall.mvp.pages;

/**
 * Created by yelong on 16/7/28.
 * mail:354734713@qq.com
 */
public interface BaseView<T> {

    void showLoadingDialog();

    void hideLoadingDialog();

    //void setPresenter(T presenter);

}
