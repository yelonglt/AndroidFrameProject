package com.dmall.mvp.pages.login;

import com.dmall.mvp.dto.User;

/**
 * Created by yelong on 16/7/29.
 * mail:354734713@qq.com
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View loginView;

    public LoginPresenter(LoginContract.View loginView) {
        this.loginView = loginView;
    }

    @Override
    public void login() {
        loginView.showLoadingDialog();

        //loginView.hideLoadingDialog();
        if ("yelong".equals(loginView.getUserName()) && "123".equals(loginView.getPassword())) {
            loginView.showLoginSuccess(new User("yelong", "123"));
        } else {
            loginView.showLoginFailed();
        }


    }
}
