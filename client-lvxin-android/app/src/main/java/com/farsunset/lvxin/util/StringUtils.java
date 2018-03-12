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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;


public class StringUtils {

    private StringUtils() {
    }


    public static boolean isEmpty(Object obj) {

        return null == obj || "".equals(obj.toString().trim());
    }


    public static String toString(Object obj) {

        return null == obj ? null : obj.toString();
    }

    public static boolean isNotEmpty(Object obj) {

        return !isEmpty(obj);
    }


    public static String getUUID() {

        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    public static boolean isWebUrl(String path) {
        if (!path.toLowerCase().startsWith("http://") && !path.toLowerCase().startsWith("https://")) {
            return false;
        }
        try {
            new URL(path);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

}
