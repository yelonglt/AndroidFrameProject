package com.dmall.mvp.pages.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dmall.mvp.R;
import com.dmall.mvp.base.BaseFragment;
import com.dmall.mvp.dto.User;
import com.google.common.base.Preconditions;

/**
 * 登录页面
 * Created by yelong on 16/7/29.
 * mail:354734713@qq.com
 */
public class LoginFragment extends BaseFragment implements LoginContract.View {
    public static final String USER_NAME = "username";

    private String username;

    private LoginContract.Presenter mPresenter;

    private EditText mUserName;
    private EditText mPassword;

    private Button mLogin;
    private Button mCancel;

    private ProgressBar mProgressBar;

    public static LoginFragment newInstance(String username) {
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USER_NAME, username);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mUserName = (EditText) view.findViewById(R.id.et_username);
        mPassword = (EditText) view.findViewById(R.id.et_password);
        mLogin = (Button) view.findViewById(R.id.btn_login);
        mCancel = (Button) view.findViewById(R.id.btn_cancel);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.login();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            username = getArguments().getString(USER_NAME);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public String getUserName() {
        return TextUtils.isEmpty(username) ? mUserName.getText().toString().trim() : username;
    }

    @Override
    public String getPassword() {
        return mPassword.getText().toString().trim();
    }

    @Override
    public void showLoginSuccess(User user) {
        Toast.makeText(getHoldingActivity(), "登录成功...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginFailed() {
        Toast.makeText(getHoldingActivity(), "登录失败...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingDialog() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = Preconditions.checkNotNull(presenter);
    }
}
