package com.dmall.mvp.pages.login;

import com.dmall.mvp.dto.User;
import com.google.common.base.Preconditions;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yelong on 16/7/29.
 * mail:354734713@qq.com
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mLoginView;

    public LoginPresenter(LoginContract.View loginView) {
        mLoginView = Preconditions.checkNotNull(loginView);
        mLoginView.setPresenter(this);
    }

    @Override
    public void login() {
        Observable.just("12").map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 12;
            }
        }).subscribeOn(Schedulers.newThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //在主线程中执行
                        mLoginView.showLoadingDialog();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                mLoginView.hideLoadingDialog();
                if ("yelong".equals(mLoginView.getUserName()) && "123".equals(mLoginView.getPassword())) {
                    mLoginView.showLoginSuccess(new User("yelong", "123"));
                } else {
                    mLoginView.showLoginFailed();
                }
            }
        });
    }
}
