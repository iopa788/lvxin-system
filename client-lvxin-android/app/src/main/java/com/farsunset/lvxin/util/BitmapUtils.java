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

import android.graphics.Bitmap;

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.network.model.SNSImage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {

    public final static int MAX_SIDE = Constant.MAX_IMAGE_PIXEL;
    public final static long MAX_SIZE = FileUtils.ONE_KB * 100; //100kb

    public final static long THUMBNAIL_MAX_SIZE = FileUtils.ONE_KB * 30; //30kb
    public final static int THUMBNAIL_MAX_SIDE = Constant.THUMBNAIL_MAX_IMAGE_PIXEL;


    public static boolean saveMapBitmap2File(Bitmap bitmap, File file) {

        FileOutputStream outputStream = null;
        try {
            AppTools.creatFileQuietly(file);
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            IOUtils.closeQuietly(outputStream);
            bitmap.recycle();
        }

        return true;
    }

    public static void savePhotoBitmap2File(Bitmap bitmap, File file) {

        FileOutputStream outputStream = null;
        try {
            AppTools.creatFileQuietly(file);
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }


    public static Bitmap compress(int maxSide, long maxSize, Bitmap bitmap, File file) {

        int length = bitmap.getRowBytes() * bitmap.getHeight();

        if (length > maxSize) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int options = 100;

            int newWidth;
            int newHeight;
            if (bitmap.getWidth() > bitmap.getHeight()) {
                newWidth = bitmap.getWidth() > maxSide ? maxSide : bitmap.getWidth();
                newHeight = bitmap.getHeight() * newWidth / bitmap.getWidth();
            } else {
                newHeight = bitmap.getHeight() > maxSide ? maxSide : bitmap.getHeight();
                newWidth = bitmap.getWidth() * newHeight / bitmap.getHeight();
            }
            Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
            do {
                baos.reset();
                options -= 5;
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            } while (baos.toByteArray().length > maxSize);

            try {
                FileUtils.writeByteArrayToFile(file, baos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            IOUtils.closeQuietly(baos);
            return finalBitmap;
        } else {
            FileOutputStream outputStream = null;
            try {
                AppTools.creatFileQuietly(file);
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(outputStream);
            }
            return bitmap;
        }


    }

    public static SNSImage compressSNSImage(Bitmap bitmap) {


        String orgImage = StringUtils.getUUID();
        File file = new File(LvxinApplication.CACHE_DIR_IMAGE, orgImage);
        Bitmap newBitmap = compress(MAX_SIDE, MAX_SIZE, bitmap, file);


        SNSImage snsImage = new SNSImage();
        snsImage.ow = newBitmap.getWidth();
        snsImage.oh = newBitmap.getHeight();
        snsImage.image = orgImage;


        File thumbnailFile = new File(LvxinApplication.CACHE_DIR_IMAGE, StringUtils.getUUID());
        Bitmap thumbnail = compress(THUMBNAIL_MAX_SIDE, THUMBNAIL_MAX_SIZE, newBitmap, thumbnailFile);

        snsImage.tw = thumbnail.getWidth();
        snsImage.th = thumbnail.getHeight();

        if (thumbnail != newBitmap) {
            snsImage.thumbnail = thumbnailFile.getName();
            thumbnail.recycle();
        } else {
            snsImage.thumbnail = orgImage;
        }
        return snsImage;
    }


    /**
     * 图片进行高斯模糊处理
     *
     * @param srcBitmap
     * @param radius
     * @param canReuseInBitmap
     * @return
     */
    public static Bitmap doBlur(Bitmap srcBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = srcBitmap;
        } else {
            bitmap = srcBitmap.copy(srcBitmap.getConfig(), true);
        }
        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }
        yw = yi = 0;
        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;
        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;
            for (x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
                sir = stack[i + radius];
                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
                rbs = r1 - Math.abs(i);
                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];
                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }
}
