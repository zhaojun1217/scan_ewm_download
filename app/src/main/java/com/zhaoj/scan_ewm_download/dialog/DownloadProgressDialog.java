package com.zhaoj.scan_ewm_download.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhaoj.scan_ewm_download.R;

public class DownloadProgressDialog extends Dialog {

    private ProgressBar pbDownload;
    private TextView tvProgress;
    private TextView tvTitle;


    public DownloadProgressDialog(Context context) {
        super(context, R.style.cartDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();

        initData();

        //初始化界面控件的事件
        initEvent();

    }

    private void initData() {
        pbDownload.setProgress(0);
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {

    }


    /**
     * 初始化界面控件
     */
    private void initView() {
        pbDownload = findViewById(R.id.pbDownload);
        tvProgress = findViewById(R.id.tvProgress);
        tvTitle = findViewById(R.id.tvTitle);
    }

    /**
     * 设置进度
     *
     * @param progress 进度
     */
    public void setProgress(int progress) {
        if (progress > 0) {
            tvTitle.setText("正在下载...");
        }
        pbDownload.setProgress(progress);
        tvProgress.setText(progress + "%");
    }

}
