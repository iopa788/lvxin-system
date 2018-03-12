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


import android.content.Intent;
import android.net.Uri;

import com.farsunset.lvxin.pro.R;

import java.io.File;

public class FileUtils {

    public static int getFileIcon(String fileName) {

        String prefix = org.apache.commons.io.FilenameUtils.getExtension(fileName).toLowerCase();
        if ("txt".equalsIgnoreCase(prefix) || "sh".equalsIgnoreCase(prefix) || "log".equalsIgnoreCase(prefix) || "ini".equalsIgnoreCase(prefix) || "xml".equalsIgnoreCase(prefix)) {
            return R.drawable.icon_file_txt;
        }
        if ("doc".equalsIgnoreCase(prefix) || "docx".equalsIgnoreCase(prefix)) {
            return R.drawable.icon_file_doc;
        }
        if ("pptx".equalsIgnoreCase(prefix) || "ppt".equalsIgnoreCase(prefix)) {
            return R.drawable.icon_file_ppt;
        }
        if ("xls".equalsIgnoreCase(prefix) || "xlsx".equalsIgnoreCase(prefix)) {
            return R.drawable.icon_file_xls;
        }
        if ("gif".equalsIgnoreCase(prefix)) {
            return R.drawable.icon_file_gif;
        }
        if ("png".equalsIgnoreCase(prefix)) {
            return R.drawable.icon_file_png;
        }
        if ("mp3".equalsIgnoreCase(prefix) || "wav".equalsIgnoreCase(prefix) || "flac".equalsIgnoreCase(prefix)) {
            return R.drawable.icon_file_mp3;
        }
        if ("pdf".equalsIgnoreCase(prefix)) {
            return R.drawable.icon_file_pdf;
        }

        if ("jpg".equalsIgnoreCase(prefix) || "jpeg".equalsIgnoreCase(prefix)) {
            return R.drawable.icon_file_jpg;
        }

        if ("rar".equalsIgnoreCase(prefix)) {
            return R.drawable.icon_file_rar;
        }
        if ("zip".equalsIgnoreCase(prefix) || "7z".equalsIgnoreCase(prefix)) {
            return R.drawable.icon_file_zip;
        }
        if ("html".equalsIgnoreCase(prefix) || "htm".equalsIgnoreCase(prefix)) {
            return R.drawable.icon_file_html;
        }


        return R.drawable.icon_file_def;
    }


    public static Intent getFileIntent(String fileName) {

        String prefix = org.apache.commons.io.FilenameUtils.getExtension(fileName).toLowerCase();
        if ("txt".equalsIgnoreCase(prefix) || "sh".equalsIgnoreCase(prefix) || "log".equalsIgnoreCase(prefix) || "ini".equalsIgnoreCase(prefix) || "xml".equalsIgnoreCase(prefix)) {
            return getTextFileIntent(fileName);
        }
        if ("doc".equalsIgnoreCase(prefix) || "docx".equalsIgnoreCase(prefix)) {
            return getWordFileIntent(fileName);
        }
        if ("pptx".equalsIgnoreCase(prefix) || "ppt".equalsIgnoreCase(prefix)) {
            return getPptFileIntent(fileName);
        }
        if ("xls".equalsIgnoreCase(prefix) || "xlsx".equalsIgnoreCase(prefix)) {
            return getExcelFileIntent(fileName);
        }

        if ("mp3".equalsIgnoreCase(prefix) || "wav".equalsIgnoreCase(prefix) || "flac".equalsIgnoreCase(prefix)) {
            return getAudioFileIntent(fileName);
        }
        if ("pdf".equalsIgnoreCase(prefix)) {
            return getPdfFileIntent(fileName);
        }

        if ("jpg".equalsIgnoreCase(prefix) || "jpeg".equalsIgnoreCase(prefix) || "png".equalsIgnoreCase(prefix) || "gif".equalsIgnoreCase(prefix)) {
            return getImageFileIntent(fileName);
        }

        if ("zip".equalsIgnoreCase(prefix) || "7z".equalsIgnoreCase(prefix) || "rar".equalsIgnoreCase(prefix)) {
            return getZipFileIntent(fileName);
        }
        if ("html".equalsIgnoreCase(prefix) || "htm".equalsIgnoreCase(prefix)) {
            return getHtmlFileIntent(fileName);
        }
        if ("apk".equalsIgnoreCase(prefix)) {
            return getApkFileIntent(fileName);
        }

        return getAllIntent(fileName);


    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }


    //Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }


    //Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    //Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    //Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    //Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //Android获取一个用于打开zip文件的intent
    public static Intent getZipFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/zip");
        return intent;
    }

    //Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri2 = Uri.fromFile(new File(param));
        intent.setDataAndType(uri2, "text/plain");
        return intent;
    }

    //Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }


}
