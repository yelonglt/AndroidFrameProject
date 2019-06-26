package com.dmall.mvp.pages.main;

import com.dmall.mvp.business.TaskDataSourceImpl;
import com.dmall.mvp.business.TaskManager;
import com.google.common.base.Preconditions;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yelong on 16/7/28.
 * mail:354734713@qq.com
 */
public class MainPresenter implements MainContract.Presenter {

    public MainContract.View mMainView;

    public TaskManager mManager;

    public MainPresenter(MainContract.View mainView) {
        mMainView = Preconditions.checkNotNull(mainView);
        mMainView.setPresenter(this);
        mManager = new TaskManager(new TaskDataSourceImpl());

    }


    @Override
    public void getString() {
        //String content = mManager.getShowContent();
        //mainView.showString(content);

        //使用RxJava
        Disposable disposable = Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String input) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return mManager.getShowContent();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mMainView.showString(s);
                    }
                });


    }
}
