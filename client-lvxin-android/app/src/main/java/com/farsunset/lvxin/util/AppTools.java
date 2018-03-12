/**
 * Copyright 2013-2023 Xia Jun(3979434@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * **************************************************************************************
 * *
 * Website : http://www.farsunset.com                           *
 * *
 * **************************************************************************************
 */
package com.farsunset.lvxin.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.PermissionCompat;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.pro.BuildConfig;
import com.farsunset.lvxin.pro.R;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class AppTools {

    private AppTools() {
    }

    public static String howTimeAgo(long when) {
        GregorianCalendar then = new GregorianCalendar();
        then.setTimeInMillis(when);

        GregorianCalendar now = new GregorianCalendar();

        int format_flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL;

        if (then.get(Calendar.YEAR) != now.get(Calendar.YEAR)) {
            format_flags |= DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;
        } else if (then.get(Calendar.DAY_OF_YEAR) != now.get(Calendar.DAY_OF_YEAR)) {
            format_flags |= DateUtils.FORMAT_SHOW_DATE;
        }

        return DateUtils.formatDateTime(LvxinApplication.getInstance(), when, format_flags);
    }
    public static CharSequence getRecentTimeString(long when) {
        return DateUtils.getRelativeTimeSpanString(when,System.currentTimeMillis(),DateUtils.MINUTE_IN_MILLIS,DateUtils.FORMAT_ABBREV_RELATIVE);
    }
    public static void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public static Bitmap toGrayscale(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap faceIconGreyBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }


    public static String getDateTimeString(long t) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(t));
    }

    public static String getDay(long t) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(new Date(t));
    }

    public static String getMonth(long t) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format(new Date(t));
    }


    public static boolean isNetworkConnected() {
        try {
            ConnectivityManager nw = (ConnectivityManager) LvxinApplication.getInstance()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = nw.getActiveNetworkInfo();
            return networkInfo != null;

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

    }

    private static boolean checkCameraPermission(AppCompatActivity context) {
        boolean hasPermission = PermissionCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (hasPermission) {
            return true;
        }
        boolean isPermissionDenied = PermissionCompat.checkPermissionForeverDenied(context, Manifest.permission.CAMERA);
        if (isPermissionDenied) {
            CustomDialog permissionDialog = new CustomDialog(context);
            permissionDialog.setTitle(R.string.tip_permission_denied);
            permissionDialog.setMessage(context.getString(R.string.tip_permission_camera_disable));
            permissionDialog.hideCancelButton();
            permissionDialog.show();
        } else {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA}, 8);
        }
        return false;
    }
    public static void startCameraActivity(AppCompatActivity context) {

        if(!checkCameraPermission(context)){
            return;
        }

        File targetFile = new File(LvxinApplication.CACHE_DIR_IMAGE + "/" + System.currentTimeMillis() + ".jpg");
        Global.setPhotoGraphFilePath(targetFile.getAbsolutePath());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
           intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            String authority = BuildConfig.APPLICATION_ID + ".fileprovider";
            Uri contentUri = FileProvider.getUriForFile(context,authority , targetFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        }else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(targetFile));
        }
        context.startActivityForResult(intent, Constant.RESULT_CAMERA);

    }

    public static void startPhotoZoom(AppCompatActivity context, Uri uri) {

        File targetFile = new File(LvxinApplication.CACHE_DIR_IMAGE, StringUtils.getUUID() + ".jpg");
        Global.setCropPhotoFilePath(targetFile.getAbsolutePath());

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            String authority = BuildConfig.APPLICATION_ID + ".fileprovider";
            Uri contentUri = FileProvider.getUriForFile(context,authority , targetFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(targetFile));
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(targetFile));

        context.startActivityForResult(intent, Constant.RESULT_ZOOM);

    }

    /**
     * 基于余弦定理求两经纬度距离
     *
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位km
     */
    public static String transformDistance(Double lon1, Double lat1, Double lon2, Double lat2) {

        double distance = getDistance(lon1, lat1, lon2, lat2);

        if (distance > 1000) {
            return LvxinApplication.getInstance().getString(R.string.label_format_kilometre,(float)(distance / 1000));
        } else {
            MathContext v = new MathContext(0, RoundingMode.HALF_DOWN);
            BigDecimal d = new BigDecimal(distance, v);
            return LvxinApplication.getInstance().getString(R.string.label_format_metre,d.intValue());
        }

    }

    /**
     * 基于余弦定理求两经纬度距离
     *
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位m
     */
    public static double getDistance(Double lon1, Double lat1, Double lon2, Double lat2) {

        if (lon1 == null) {
            lon1 = 0d;
        }
        if (lat1 == null) {
            lat1 = 0d;
        }
        if (lon2 == null) {
            lon2 = 0d;
        }
        if (lat2 == null) {
            lat2 = 0d;
        }
        double EARTH_RADIUS = 6378.137;
        double radLat1 = lat1 * Math.PI / 180.0;
        double radLat2 = lat2 * Math.PI / 180.0;
        double a = radLat1 - radLat2;
        double b = lon1 * Math.PI / 180.0 - lon2 * Math.PI / 180.0;

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10;

        return s;
    }


    public static boolean contains(int[] loc, MotionEvent event) {
        return event.getY() >= loc[1] && event.getY() <= loc[3]
                && event.getX() >= loc[0] && event.getX() <= loc[2];
    }


    public static String getSignature(Context context) {
        try {
            /** 通过包管理器获得指定包名包含签名的包信息 **/
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            /******* 通过返回的包信息获得签名数组 *******/
            Signature[] signatures = packageInfo.signatures;
            /******* 循环遍历签名数组拼接应用签名 *******/
            return signatures[0].toCharsString();
            /************** 得到应用签名 **************/
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public static File creatSDFile(String fileName) {
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
        }
        return file;
    }

    public static int dip2px(float dip) {
        return (int) ((Resources.getSystem().getDisplayMetrics().density * dip) + 0.5f);
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     */
    public static void creatSDDir(String dirName) {
        File dir = new File(dirName);
        dir.mkdirs();
    }


    public static void creatFileQuietly(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeQuietly(final Cursor closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception ioe) {
        }
    }
}
