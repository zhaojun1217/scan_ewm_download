package com.zhaoj.scan_ewm_download;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhaoj.scan_ewm_download.common.Constant;
import com.zhaoj.scan_ewm_download.dialog.DialogUtil;
import com.zhaoj.scan_ewm_download.download.DownLoadUtil;
import com.zhaoj.scan_ewm_download.utils.BitmapHandleUtil;
import com.zhaoj.scan_ewm_download.utils.CreateBarcodeUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import gdut.bsx.share2.FileUtil;
import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private int REQUEST_CODE = 2333;
    private static final int RERMISSION_REQUESTCODE = 1;
    private Button btnScan;
    private ImageView imgScanRes;
    private TextView tvLongCheckText;
    private File fileByBitmap;
    protected String[] needPermission = {android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 获取通知权限
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ZXingLibrary.initDisplayOpinion(this);
        DownLoadUtil.init(this);
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);   // 初始化 JPush

        initView();

        initEvent();


        boolean enabled = isNotificationEnabled(MainActivity.this);

        if (!enabled) {
            /**
             * 跳到通知栏设置界面
             * @param context
             */
            Intent localIntent = new Intent();
            //直接跳转到应用通知设置的代码：
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                localIntent.putExtra("app_package", getPackageName());
                localIntent.putExtra("app_uid", getApplicationInfo().uid);
            } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                localIntent.addCategory(Intent.CATEGORY_DEFAULT);
                localIntent.setData(Uri.parse("package:" + getPackageName()));
            } else {
                //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    localIntent.setAction(Intent.ACTION_VIEW);
                    localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                    localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                }
            }
            startActivity(localIntent);
        }


    }

    private void initEvent() {
        /**
         * 打开默认二维码扫描界面
         */
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EasyPermissions.hasPermissions(MainActivity.this, needPermission)) {
                    EasyPermissions.requestPermissions(MainActivity.this, getString(R.string.app_need_this_permission), RERMISSION_REQUESTCODE, needPermission);
                } else {
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });

        tvLongCheckText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Share2.Builder(MainActivity.this)
                        .setContentType(ShareContentType.IMAGE)
                        .setShareFileUri(getShareImageUrl())
                        .setTitle("分享")
                        .build()
                        .shareBySystem();
            }
        });

        imgScanRes.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Bitmap obmp = ((BitmapDrawable) (imgScanRes).getDrawable()).getBitmap();
                int width = obmp.getWidth();
                int height = obmp.getHeight();
                int[] data = new int[width * height];
                obmp.getPixels(data, 0, width, 0, 0, width, height);
                RGBLuminanceSource source = new RGBLuminanceSource(width, height, data);
                BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
                QRCodeReader reader = new QRCodeReader();
                Result re = null;
                try {
                    re = reader.decode(bitmap1);
                } catch (NotFoundException e) {
                    e.printStackTrace();
                } catch (ChecksumException e) {
                    e.printStackTrace();
                } catch (FormatException e) {
                    e.printStackTrace();
                }

                // 检测到下载地址为xxx，是否下载
                DialogUtil.showCustomCommonDialog(MainActivity.this, "检测到下载地址,是否下载？", re.getText());
                return false;

            }
        });
    }

    private Uri getShareImageUrl() {
//        MediaStore.Images.Media.insertImage(getContentResolver(),new Bitmap(),"","" );
        return FileUtil.getFileUri(MainActivity.this, ShareContentType.IMAGE, fileByBitmap);
    }

    private void initView() {
        btnScan = findViewById(R.id.btnScan);
        tvLongCheckText = findViewById(R.id.tvLongCheckText);
        imgScanRes = findViewById(R.id.imgScanRes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 动态申请权限
        if (!EasyPermissions.hasPermissions(MainActivity.this, needPermission)) {
            EasyPermissions.requestPermissions(MainActivity.this, getString(R.string.app_need_this_permission), RERMISSION_REQUESTCODE, needPermission);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    final String result = bundle.getString(CodeUtils.RESULT_STRING);

                    int state = getPackageManager().getApplicationEnabledSetting(BuildConfig.APPLICATION_ID);
                    //检测下载管理器是否被禁用
                    if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                            || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                            || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("温馨提示").setMessage
                                ("系统下载管理器被禁止，需手动打开").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                    startActivity(intent);
                                }

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();

                    } else {

                        // 生成二维码展示
                        Bitmap scanResBitmap = CreateBarcodeUtil.createImage(result, 500, 500, BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
                        imgScanRes.setImageBitmap(scanResBitmap);
                        fileByBitmap = BitmapHandleUtil.createFileByBitmap(scanResBitmap, Constant.DIRPATH);
                        tvLongCheckText.setVisibility(View.VISIBLE);
                        // 检测到下载地址为xxx，是否下载
                        DialogUtil.showCustomCommonDialog(MainActivity.this, "检测到下载地址,是否下载？", result);
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //处理权限名字字符串
        StringBuffer sb = new StringBuffer();
        for (String str : perms) {
            sb.append(str);
            sb.append("\n");
        }
        sb.replace(sb.length() - 2, sb.length(), "");
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toast.makeText(this, "已拒绝权限" + sb + "并不再询问", Toast.LENGTH_SHORT).show();
            new AppSettingsDialog
                    .Builder(this)
                    .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                    .setPositiveButton("是")
                    .setNegativeButton("否")
                    .build()
                    .show();
        }
    }
}
