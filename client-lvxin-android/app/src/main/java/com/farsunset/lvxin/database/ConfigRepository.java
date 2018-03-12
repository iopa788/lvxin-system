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

import com.farsunset.lvxin.model.Config;

import java.util.HashMap;
import java.util.List;

public class ConfigRepository extends BaseRepository<Config, String> {

    private static ConfigRepository manager = new ConfigRepository();

    public static HashMap<String, String> queryConfigs() {

        List<Config> list = manager.innerQueryAll();
        HashMap<String, String> map = new HashMap<String, String>(list.size());

        for (Config c : list) {
            map.put(c.key, c.value);

        }
        return map;
    }

    public static void add(String key, String value) {
        Config config = new Config();
        config.key = key;
        config.value = value;
        manager.createOrUpdate(config);
    }

    public static String queryValue(String key) {

        Config config = manager.innerQueryById(key);
        return config == null ? null : config.value;
    }

    public static void delete(String key) {
        manager.innerDeleteById(key);
    }
}
