package com.dmall.mvp.pages.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.dmall.mvp.R;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.Presenter mPresenter;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.message_content);

        mPresenter = new MainPresenter(this);
        mPresenter.getString();

    }

    @Override
    public void showString(String content) {
        mTextView.setText(content);
    }
}
