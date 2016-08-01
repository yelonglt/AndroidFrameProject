package com.dmall.mvp.pages.login;

import com.dmall.mvp.dto.User;
import com.dmall.mvp.pages.BasePresenter;
import com.dmall.mvp.pages.BaseView;

/**
 * Created by yelong on 16/7/29.
 * mail:354734713@qq.com
 */
public class LoginContract {

    interface View extends BaseView<Presenter> {
        String getUserName();

        String getPassword();

        void showLoginSuccess(User user);

        void showLoginFailed();
    }

    interface Presenter extends BasePresenter {
        void login();
    }
}
