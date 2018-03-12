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


public class CloudImageLoaderFactory {
    public static final int MODE_GLIDE = 0;
    public static final int MODE_FRESC0 = 1;
    private static GlideImageLoader glideImageLoader = new GlideImageLoader();

    public static CloudImageLoader get() {
        return glideImageLoader;
    }

    public static CloudImageLoader get(int mode) {
        if (mode == MODE_GLIDE) {
            return glideImageLoader;
        }
        return glideImageLoader;
    }
}
