package com.dmall.mvp.pages.main;

import com.dmall.mvp.business.TaskDataSourceImpl;
import com.dmall.mvp.business.TaskManager;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yelong on 16/7/28.
 * mail:354734713@qq.com
 */
public class MainPresenter implements MainContract.Presenter {

    public MainContract.View mainView;

    public TaskManager mManager;

    public MainPresenter(MainContract.View mainView) {
        this.mainView = mainView;
        mManager = new TaskManager(new TaskDataSourceImpl());
    }


    @Override
    public void getString() {
        //String content = mManager.getShowContent();
        //mainView.showString(content);

        //使用RxJava
        Observable.just("")
                .subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return mManager.getShowContent();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mainView.showString(s);
                    }
                });


    }
}
