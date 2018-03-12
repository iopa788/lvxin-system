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
package com.farsunset.lvxin.database;


import android.support.v4.util.ArrayMap;

import com.farsunset.lvxin.model.GlideImage;

public class GlideImageRepository extends BaseRepository<GlideImage, String> {
    public final static String DATABASE_NAME = "common.db";
    private final static ArrayMap<String, String> GLIDE_IMAGE_MAP = new ArrayMap<String, String>(20);
    private static GlideImageRepository manager = new GlideImageRepository();


    /**
     * 图片版本信息为公共数据，不需要清除
     */
    @Override
    public  void clearTable() {
    }

    @Override
    public String getDatabaseName() {

        return DATABASE_NAME;
    }

    public static String getVersion(String url) {
        if (url == null) {
            return null;
        }
        String version = GLIDE_IMAGE_MAP.get(url);
        if (version == null) {
            GlideImage config = manager.innerQueryById(url);
            if (config != null) {
                version = config.version;
                GLIDE_IMAGE_MAP.put(url, version);
            }
        }
        if (version == null) {
            GLIDE_IMAGE_MAP.put(url, "");
        }
        return version;
    }

    public static void save(String url, String version) {
        GlideImage config = new GlideImage();
        config.url = url;
        config.version = version;
        manager.createOrUpdate(config);
        GLIDE_IMAGE_MAP.put(url, version);
    }
}
