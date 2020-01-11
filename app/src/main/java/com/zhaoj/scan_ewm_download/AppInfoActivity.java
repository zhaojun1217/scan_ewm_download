package com.zhaoj.scan_ewm_download;

import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaoj.scan_ewm_download.base.BaseActivity;

public class AppInfoActivity extends BaseActivity {
    private RefreshLayout refreshLayout;
    private TextView tvAppVersionName;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_app_info;
    }

    @Override
    protected void initView() {
        super.initView();
        refreshLayout = findViewById(R.id.refreshLayout);
        tvAppVersionName = findViewById(R.id.tvAppVersionName);
        tvAppVersionName.setText("当前版本 ：" + BuildConfig.VERSION_NAME);
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }
}
