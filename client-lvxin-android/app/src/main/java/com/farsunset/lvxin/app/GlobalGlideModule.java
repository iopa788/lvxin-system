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
package com.farsunset.lvxin.app;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.target.ViewTarget;
import com.farsunset.lvxin.pro.R;

@GlideModule
public class GlobalGlideModule extends AppGlideModule {
    /**
     * 500 MB of cache.
     */
    final static int DEFAULT_DISK_CACHE_SIZE = 500 * 1024 * 1024;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setLogLevel(Log.ERROR);
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, "image", DEFAULT_DISK_CACHE_SIZE));
        //设置glide加载图片的 tag，解决 WebImageVew不能直接 setTag(Object tag)的问题
        ViewTarget.setTagId(R.id.GLIDE_TAG);
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

}
