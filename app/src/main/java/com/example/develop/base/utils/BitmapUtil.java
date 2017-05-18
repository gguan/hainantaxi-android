package com.example.develop.base.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by develop on 17-5-17.
 */
public class BitmapUtil {
    public static final String TAG = BitmapUtil.class.getSimpleName();

    private BitmapUtil() {

    }

    public static String cropBitmap(String path, int maxWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        Bitmap originBitmap = BitmapFactory.decodeFile(path, options);

        int width = options.outWidth;
        int height = options.outHeight;

        float scaleFactor = (float) maxWidth / width;


        if (scaleFactor < 1) {
            width = maxWidth;
            height = (int) (height * scaleFactor);
        } else {
            return path;
        }


        options.inJustDecodeBounds = false;

        originBitmap = BitmapFactory.decodeFile(path, options);

        Bitmap scaledBitmap;

        if (originBitmap.getWidth() == width && originBitmap.getHeight() == height) {
            scaledBitmap = originBitmap;
        } else {
            scaledBitmap = Bitmap.createScaledBitmap(originBitmap, width, height, false);
            originBitmap.recycle();
        }

        return saveImageToGallery(scaledBitmap);
    }


    public static String saveImageToGallery(Bitmap bmp) {
        if (bmp == null) {
            return "";
        }
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Playalot");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();

    }

    // 把文件插入到系统图库
    public static void notifySystemGallery(Context context, File file) {
        if (file == null || !file.exists()) {
            return;
        }

        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }
}
