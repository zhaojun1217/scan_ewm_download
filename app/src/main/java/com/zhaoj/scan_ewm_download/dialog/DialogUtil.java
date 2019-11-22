package com.zhaoj.scan_ewm_download.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.zhaoj.scan_ewm_download.download.DownLoadUtil;
import com.zhaoj.scan_ewm_download.R;

import pub.devrel.easypermissions.EasyPermissions;

public class DialogUtil {
    private static String[] needPermission = {Manifest.permission.CALL_PHONE};
    private static DownloadProgressDialog downloadProgressDialog;

    @SuppressLint("MissingPermission")
    public static void callDefaultPhone(final Context context, final String mobile) {
        Dialog dialog = null;
        // 联系
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(mobile)
                .setPositiveButton(R.string.order_dialog_confirm,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!EasyPermissions.hasPermissions(context, needPermission)) {
                                    EasyPermissions.requestPermissions((Activity) context, context.getString(R.string.app_need_this_permission), 1, needPermission);
                                } else {
                                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile)));
                                }
                            }
                        })
                .setNegativeButton("取消", null);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    @SuppressLint("MissingPermission")
    public static void callPhoneTwo(final Context mContext, final String mobile) {
        final CommonCallDialog mCommonCallDialog = new CommonCallDialog(mContext);
        mCommonCallDialog.show();

        mCommonCallDialog.setYesOnclickListener(new CommonCallDialog.onYesOnclickListener() {
            @Override
            public void onYesClick(String msg) {
                if (!EasyPermissions.hasPermissions(mContext, needPermission)) {
                    EasyPermissions.requestPermissions((Activity) mContext, mContext.getString(R.string.app_need_this_permission), 1, needPermission);
                } else {
                    mContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile)));
                }
            }
        });

        mCommonCallDialog.setNoOnclickListener(new CommonCallDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                mCommonCallDialog.dismiss();
            }
        });
        mCommonCallDialog.setTextView(mobile);
    }

    public static void showCustomCommonDialog(final Context mContext, final String title, String msg) {

        final CommonShowDialog commonShowDialog = new CommonShowDialog(mContext);
        commonShowDialog.show();
        commonShowDialog.setYesOnclickListener(new CommonShowDialog.onYesOnclickListener() {
            @Override
            public void onYesClick(String msg) {
                DownLoadUtil.downApkByUrl(msg);
                commonShowDialog.dismiss();
            }
        });
        commonShowDialog.setNoOnclickListener(new CommonShowDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                commonShowDialog.dismiss();
            }
        });
        commonShowDialog.setTitle(title);
        commonShowDialog.setTextView(msg);
    }

    public static void showDownloadDialog(Context mContext) {
        if (downloadProgressDialog == null) {
            downloadProgressDialog = new DownloadProgressDialog(mContext);
        }
        if (!downloadProgressDialog.isShowing()) {
            downloadProgressDialog.show();
        }
    }

    public static void setDownLoadProgress(int progress) {
        if (downloadProgressDialog != null && downloadProgressDialog.isShowing()) {
            downloadProgressDialog.setProgress(progress);
        }
    }

    public static void closeDownLoadDialog() {
        if (downloadProgressDialog != null && downloadProgressDialog.isShowing()) {
            downloadProgressDialog.dismiss();
        }
    }
}
