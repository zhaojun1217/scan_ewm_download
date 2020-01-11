package com.zhaoj.scan_ewm_download.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import com.zhaoj.scan_ewm_download.base.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class SystemUtil {

    /**
     * 检查WIFI是否连接
     */
    public static boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiInfo != null && wifiInfo.isConnected();
    }

    /**
     * 检查手机网络(4G/3G/2G)是否连接
     */
    public static boolean isMobileNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return mobileNetworkInfo != null && mobileNetworkInfo.isConnected();
    }

    /**
     * 检查是否有可用网络
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    /**
     * 保存文字到剪贴板
     *
     * @param context
     * @param text
     */
    public static void copyToClipBoard(Context context, String text) {
        ClipData clipData = ClipData.newPlainText("url", text);
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(clipData);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dp2px(float dpValue) {
        final float scale = MyApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2dp(float pxValue) {
        final float scale = MyApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据进程号获取对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取app的版本号
     * @param activity
     * @return
     */
    public static String getAppVersionName(Activity activity) {
        String versionName = "";
        try {
            PackageManager pm = activity.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(activity.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static Map<String, String> stringStringMapForRecurit() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("HouseholdRegister", "户籍要求");
        result.put("NationInfo", "民族要求");
        result.put("IDCardType", "身份证允许类型");
        result.put("Qualification", "毕业证要求");
        result.put("Characters", "英文字母");
        result.put("Math", "简单算术");
        result.put("Exam", "是否考试");
        result.put("Clothes", "无尘服");
        result.put("ForeignBodies", "体内金属");
        result.put("Physical", "是否体检");
        result.put("FaceRecognition", "人脸识别");
        result.put("CheckCriminal", "是否查案底");
        result.put("Body", "身体状况");
        result.put("Tattoo", "纹身要求");
        result.put("SmokeScar", "烟疤要求");
        result.put("MaleMinHeight-FemaleMinHeight", "身高要求");
        result.put("FactoryPickup", "厂门口接站");
        result.put("Return", "返厂规定");
        result.put("GiftName-GiftAmount", "赠品内容");
        return result;
    }

    public static JSONObject recuritListConditionJason() throws JSONException {
        String str = "{\"IDCardType\":{\"multi\":true,\"values\":[{\"enum\":\"7\",\"str\":\"不限\"}," +
                "{\"enum\":\"1\",\"str\":\"必须有磁身份证\"},{\"enum\":\"2\",\"str\":\"可以无磁身份证\"}," +
                "{\"enum\":\"4\",\"str\":\"可以临时身份证\"}],\"remark\":\"\"},\"Qualification\":{\"multi\":true," +
                "\"values\":[{\"enum\":\"31\",\"str\":\"不限\"},{\"enum\":\"1\",\"str\":\"小学毕业证\"}," +
                "{\"enum\":\"2\",\"str\":\"初中毕业证\"},{\"enum\":\"4\",\"str\":\"高中毕业证\"}," +
                "{\"enum\":\"8\",\"str\":\"大专毕业证\"},{\"enum\":\"16\",\"str\":\"本科及以上毕业证\"}]," +
                "\"remark\":\"\"},\"Tattoo\":{\"multi\":true,\"values\":[{\"enum\":\"31\",\"str\":\"不限\"}," +
                "{\"enum\":\"16\",\"str\":\"全身无纹身\"},{\"enum\":\"1\",\"str\":\"手部无纹身\"}," +
                "{\"enum\":\"2\",\"str\":\"手臂无纹身\"},{\"enum\":\"4\",\"str\":\"上身无纹身\"}," +
                "{\"enum\":\"8\",\"str\":\"无攻击性纹身\"}],\"remark\":\"TattooRemark\"}," +
                "\"SmokeScar\":{\"multi\":true,\"values\":[{\"enum\":\"15\",\"str\":\"不限\"}," +
                "{\"enum\":\"8\",\"str\":\"全身无烟疤\"},{\"enum\":\"1\",\"str\":\"手部无烟疤\"}," +
                "{\"enum\":\"2\",\"str\":\"手臂无烟疤\"},{\"enum\":\"4\",\"str\":\"上身无烟疤\"}]," +
                "\"remark\":\"SmokeScarRemark\"},\"Characters\":{\"multi\":false," +
                "\"values\":{\"2\":\"无规定\",\"0\":\"没要求\",\"1\":\"听说读写26个字母\"}," +
                "\"remark\":\"CharactersRemark\"},\"Math\":{\"multi\":false," +
                "\"values\":{\"2\":\"无规定\",\"0\":\"没要求\",\"1\":\"会简单加减乘除\"}," +
                "\"remark\":\"MathRemark\"},\"Exam\":{\"multi\":false,\"values\":{\"3\":\"无规定\",\"0\":\"不考试\",\"1\":\"简单考试\",\"2\":\"考试比较难\"}," +
                "\"remark\":\"ExamRemark\"},\"CheckCriminal\":{\"multi\":false," +
                "\"values\":{\"2\":\"无规定\",\"0\":\"查案底\",\"1\":\"不查案底\"},\"remark\":\"\"}," +
                "\"FaceRecognition\":{\"multi\":false,\"values\":{\"2\":\"无规定\",\"0\":\"没有\",\"1\":\"有\"}," +
                "\"remark\":\"\"},\"Clothes\":{\"multi\":false,\"values\":{\"3\":\"无规定\",\"0\":\"看部门\",\"1\":\"穿\",\"2\":\"不穿\"}," +
                "\"remark\":\"\"},\"Physical\":{\"multi\":false,\"values\":{\"2\":\"无规定\",\"0\":\"不需要\",\"1\":\"需要\"},\"remark\":\"\"}," +
                "\"ForeignBodies\":{\"multi\":false,\"values\":{\"2\":\"无规定\",\"0\":\"不查\",\"1\":\"查\"},\"remark\":\"\"}}";

        return new JSONObject(str);
    }

    private static final int DEF_DIV_SCALE = 10;

    /**
     * 两个Double数相加 BigDecimal精确计算小数
     *.doubleValue();对原值进行操作,不然回new出来一个新对象,返回的原值却没有变
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double add(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.add(b2).doubleValue();
    }

    /**
     * 两个Double数相减 BigDecimal精确计算小数
     *
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double sub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 两个Double数相乘
     *
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double mul(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 两个Double数相除
     *
     * @param v1
     * @param v2
     * @return Double
     */
    public static Double div(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 两个Double数相除，并保留scale位小数
     *
     * @param v1
     * @param v2
     * @param scale
     * @return Double
     */
    public static Double div(Double v1, Double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }
}
