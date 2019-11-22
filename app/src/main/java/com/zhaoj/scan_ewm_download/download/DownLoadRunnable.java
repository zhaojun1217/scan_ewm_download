package com.zhaoj.scan_ewm_download.download;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by $zhao on 2018/7/5.
 */

public class DownLoadRunnable implements Runnable {

    private String url;
    private Handler handler;
    private Context mContext;

    public DownLoadRunnable(Context context, String url, Handler handler) {
        this.mContext = context;
        this.url = url;
        this.handler = handler;
    }

    @Override
    public void run() {
        Looper.prepare();//创建与当前线程相关的Looper
        //设置线程优先级为后台，这样当多个线程并发后很多无关紧要的线程分配的CPU时间将会减少，有利于主线程的处理
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        //具体下载方法
        startDownload();
        Looper.loop();
    }


    private void startDownload() {
        //获得DownloadManager对象
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
        //获得下载id，这是下载任务生成时的唯一id，可通过此id获得下载信息
        if (CreateRequest(url) != null) {
            long requestId = downloadManager.enqueue(CreateRequest(url));
            //查询下载信息方法
            queryDownloadProgress(requestId, downloadManager);
        }
    }

    private void queryDownloadProgress(long requestId, DownloadManager downloadManager) {

        DownloadManager.Query query = new DownloadManager.Query();
        //根据任务编号id查询下载任务信息
        query.setFilterById(requestId);
        try {
            boolean isGoging = true;
            while (isGoging) {
                Cursor cursor = downloadManager.query(query);
                if (cursor != null && cursor.moveToFirst()) {
                    //获得下载状态
                    int state = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (state) {
                        case DownloadManager.STATUS_SUCCESSFUL://下载成功
                            isGoging = false;
                            handler.obtainMessage(downloadManager.STATUS_SUCCESSFUL).sendToTarget();//发送到主线程，更新ui
                            break;
                        case DownloadManager.STATUS_FAILED://下载失败
                            isGoging = false;
                            handler.obtainMessage(downloadManager.STATUS_FAILED).sendToTarget();//发送到主线程，更新ui
                            break;
                        case DownloadManager.STATUS_RUNNING://下载中
                            /**
                             * 计算下载下载率；
                             */
                            int totalSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            int currentSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            int progress = (int) (((float) currentSize) / ((float) totalSize) * 100);
                            handler.obtainMessage(downloadManager.STATUS_RUNNING, progress).sendToTarget();//发送到主线程，更新ui
                            break;

                        case DownloadManager.STATUS_PAUSED://下载停止
                            handler.obtainMessage(DownloadManager.STATUS_PAUSED).sendToTarget();
                            break;

                        case DownloadManager.STATUS_PENDING://准备下载
                            handler.obtainMessage(DownloadManager.STATUS_PENDING).sendToTarget();
                            break;
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DownloadManager.Request CreateRequest(String url) {
        DownloadManager.Request request = null;
        try {
            request = new DownloadManager.Request(Uri.parse(url));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);// 隐藏notification
            request.allowScanningByMediaScanner();
            request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS,   "download.apk");//指定APK缓存路径和应用名称，可在SD卡/Android/data/包名/file/Download文件夹中查看
        } catch (Exception e) {
            Toast.makeText(mContext, "下载地址有误:" + url, Toast.LENGTH_SHORT).show();
            handler.obtainMessage(DownloadManager.STATUS_FAILED).sendToTarget();
        }
        return request;
    }

}
