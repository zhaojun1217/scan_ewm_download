package com.zhaoj.scan_ewm_download.download;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.zhaoj.scan_ewm_download.dialog.DialogUtil;

import java.io.File;

public class DownLoadUtil {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static void downApkByUrl(String downLoadUrl) {

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            @RequiresApi(api = Build.VERSION_CODES.FROYO)
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DownloadManager.STATUS_SUCCESSFUL:
                        DialogUtil.closeDownLoadDialog();
                        Toast.makeText(mContext, "下载任务已经完成！", Toast.LENGTH_SHORT).show();
                        Intent installintent = new Intent();
                        installintent.setAction(Intent.ACTION_VIEW);
                        // 在Boradcast中启动活动需要添加Intent.FLAG_ACTIVITY_NEW_TASK
                        installintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri contentUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + "download.apk"));
                            installintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            installintent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                        } else {
                            installintent.setDataAndType(Uri.fromFile(new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + "download.apk")),
                                    "application/vnd.android.package-archive");
                        }
                        mContext.startActivity(installintent);
                        break;

                    case DownloadManager.STATUS_RUNNING:
                        Log.e("XX", "" + (int) msg.obj);
                        DialogUtil.setDownLoadProgress((int) msg.obj);
                        break;

                    case DownloadManager.STATUS_FAILED:
                        DialogUtil.closeDownLoadDialog();
                        break;

                    case DownloadManager.STATUS_PENDING:
                        break;
                }
            }
        };
        // 发现有新版本需要安装的时候,直接先清空装在apk的文件夹 ,如果是第一次安装，防止找不到文件夹报错，try catch
        try {
            clearTemp(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        } catch (Exception e) {

        }
        DialogUtil.showDownloadDialog(mContext);
        new Thread(new DownLoadRunnable(mContext, downLoadUrl, handler)).start();

    }

    public static void clearTemp(String dirPath) {
        File dir = new File(dirPath);//清空文件夹
        File[] files = dir.listFiles();
        if (null != files) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                boolean b = file.delete();
            }
        }
    }

}
