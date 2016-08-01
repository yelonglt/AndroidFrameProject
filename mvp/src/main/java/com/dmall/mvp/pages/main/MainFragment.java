package com.dmall.mvp.pages.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.dmall.mvp.R;
import com.dmall.mvp.base.BaseFragment;
import com.dmall.mvp.pages.login.LoginActivity;
import com.google.common.base.Preconditions;

/**
 * Created by yelong on 16/8/1.
 * mail:354734713@qq.com
 */
public class MainFragment extends BaseFragment implements MainContract.View {

    private TextView mTextView;

    private MainContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mTextView = (TextView) view.findViewById(R.id.message_content);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPresenter.getString();

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getHoldingActivity(), LoginActivity.class);
                startActivity(intent);

                /*LoginFragment loginFragment = new LoginFragment();
                new LoginPresenter(loginFragment);
                addFragment(loginFragment);*/
            }
        });
    }

    @Override
    public void showString(String content) {
        mTextView.setText(content);
    }

    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void hideLoadingDialog() {

    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = Preconditions.checkNotNull(presenter);
    }
}
