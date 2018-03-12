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
package com.farsunset.lvxin.network;


import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.farsunset.lvxin.listener.CloudImageApplyListener;
import com.farsunset.lvxin.listener.CloudImageLoadListener;

import java.io.File;

public interface CloudImageLoader {

    void loadAndApply(ImageView target, String url, @DrawableRes int defResId, float round, CloudImageApplyListener listener);

    void loadAndApply(ImageView target, File file, @DrawableRes int defResId, float round, CloudImageApplyListener listener);

    void loadAndApply(ImageView target, File file, @DrawableRes int defResId, float round);

    void loadAndApply(ImageView target, String url, @DrawableRes int defResId, float round);

    void loadAndApply(ImageView target, File file, @DrawableRes int defResId);

    void loadAndApply(ImageView target, String url, @DrawableRes int defResId);

    void loadAndApply(ImageView target, File file, CloudImageApplyListener listener);

    void loadAndApply(ImageView target, String url, CloudImageApplyListener listener);

    void loadAndApply(ImageView target, String url, Bitmap defResBitmap, CloudImageApplyListener listener);

    void loadAndApply(ImageView target, File file, @DrawableRes int defResId, CloudImageApplyListener listener);

    void loadAndApply(ImageView target, String url, @DrawableRes int defResId, CloudImageApplyListener listener);

    void loadGifAndApply(ImageView target, String url);

    void downloadOnly(String url);

    void downloadOnly(String url, CloudImageLoadListener listener);

    void downloadOnly(File file, CloudImageLoadListener listener);

    void downloadOnly(Uri file, CloudImageLoadListener listener);

    void downloadOnly(Uri file, int size, CloudImageLoadListener listener);

    void clearMemory();

    void clearDiskCache();

}
