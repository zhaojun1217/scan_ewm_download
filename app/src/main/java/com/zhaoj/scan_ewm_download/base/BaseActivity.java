package com.zhaoj.scan_ewm_download.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    protected Activity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mContext = this;
        // 初始化控件
        initView();
        // 初始化点击事件
        initEvent();
        // 初始化页面上的工具
        initPage();
    }

    protected abstract int getContentViewId();

    protected void initPage() {
    }

    protected void initView() {
    }

    protected void initEvent() {
    }

}
