package com.zhaoj.scan_ewm_download.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class BitmapHandleUtil {

    public static File createFileByBitmap(Bitmap btImage, String sdCardDir) {
        File file = null;
        FileOutputStream out = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
        {      // 获取SDCard指定目录下
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {             //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }                            //文件夹有啦，就可以保存图片啦
            file = new File(sdCardDir, UUID.randomUUID() + ".jpg");// 在SDcard的目录下创建图片文,以当前时间为其命名
            try {
                out = new FileOutputStream(file);
                btImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}
