package com.dmall.mvp.pages.login;

import com.dmall.mvp.dto.User;
import com.google.common.base.Preconditions;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


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
        Disposable disposable = Observable.just("12")
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) throws Exception {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return 12;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //在主线程中执行
                        mLoginView.showLoadingDialog();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        mLoginView.hideLoadingDialog();
                        if ("yelong".equals(mLoginView.getUserName()) && "123456".equals(mLoginView.getPassword())) {
                            mLoginView.showLoginSuccess(new User("yelong", "123456"));
                        } else {
                            mLoginView.showLoginFailed();
                        }
                    }
                });
        disposable.isDisposed();
    }
}
