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

import android.net.Uri;

import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;

import java.io.File;

/**
 * 阿里云 OSS 文件 地址构建
 */
public class FileURLBuilder {

    //用户头像存储bucket
    public static final String BUCKET_USER_ICON = "user-icon";

    //群头像存储bucket
    public static final String BUCKET_GROUP_ICON = "group-icon";

    //公众号logo 存储 bucket
    public static final String BUCKET_PUBACCOUNT_ICON = "pub-icon";

    //聊天文件，朋友圈图片等其他文件存储bucket
    public static final String BUCKET_FILES = "other-files";

    private FileURLBuilder() {
    }

    public static String getUserIconUrl(String account) {
        return String.format(URLConstant.FILE_PATH_URL, BUCKET_USER_ICON, account);
    }

    public static String getPubAccountLogoUrl(String account) {
        return String.format(URLConstant.FILE_PATH_URL, BUCKET_PUBACCOUNT_ICON, account);
    }

    public static String getGroupIconUrl(String groupId) {
        return String.format(URLConstant.FILE_PATH_URL, BUCKET_GROUP_ICON, groupId);
    }

    public static String getFileUrl(String name, String key) {
        return String.format(URLConstant.FILE_PATH_URL, name, key);
    }

    public static String getFileUrl(String key) {
        if (key == null) {
            return null;
        }
        File file = new File(LvxinApplication.CACHE_DIR_IMAGE, key);
        if (file.exists()) {
            return Uri.fromFile(file).toString();
        }
        return String.format(URLConstant.FILE_PATH_URL, BUCKET_FILES, key);
    }

    public static boolean isLogo(String url) {
        return url.startsWith(String.format(URLConstant.FILE_PATH_URL, BUCKET_USER_ICON, ""))
                || url.startsWith(String.format(URLConstant.FILE_PATH_URL, BUCKET_PUBACCOUNT_ICON, ""))
                || url.startsWith(String.format(URLConstant.FILE_PATH_URL, BUCKET_GROUP_ICON, ""));
    }
}
