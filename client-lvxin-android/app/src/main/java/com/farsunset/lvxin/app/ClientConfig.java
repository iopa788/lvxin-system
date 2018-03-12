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
import android.content.SharedPreferences;

import java.net.MalformedURLException;
import java.net.URL;

public class ClientConfig {

    static final String MODEL_KEY = "CLIENT_CONFIG";
    static final String KEY_MESSAGE_NOTIFY_SWITCH = "KEY_MESSAGE_NOTIFY_SWITCH";
    static final String KEY_MESSAGE_SOUND_SWITCH = "KEY_MESSAGE_SOUND_SWITCH";
    static final String KEY_MESSAGE_DISTURB_SWITCH = "KEY_MESSAGE_DISTURB_SWITCH";
    static final String KEY_SHAKE_SOUND_SWITCH = "KEY_MESSAGE_SOUND_SWITCH";
    static final String KEY_CURRNET_REGION = "KEY_CURRNET_REGION";

    static final String KEY_MESSAGE_RECEIPT_SWITCH = "KEY_MESSAGE_RECEIPT_SWITCH";
    static final String KEY_MESSAGE_STATUS_SWITCH = "KEY_MESSAGE_STATUS_SWITCH";

    static final String KEY_SETTING_SERVER_PATH = "KEY_SETTING_SERVER_PATH";
    static final String KEY_SETTING_SERVER_PORT = "KEY_SETTING_SERVER_PORT";
    static final String KEY_SETTING_SERVER_HOST = "KEY_SETTING_SERVER_HOST";

    static final String KEY_SOFTKEYBORD_HEIGHT = "KEY_SOFTKEYBORD_HEIGHT";

    private ClientConfig() {
    }

    public static void setBooleanConfig(String key ,boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, flag).apply();
    }

    public static boolean getBooleanConfig(String key,boolean defValue) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void setMessageDisturbEnable(boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_MESSAGE_DISTURB_SWITCH, flag).apply();
    }

    public static boolean getMessageDisturbEnable() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_MESSAGE_DISTURB_SWITCH, true);
    }

    public static void setMessageNotifyEnable(boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_MESSAGE_NOTIFY_SWITCH, flag).apply();
    }


    public static boolean getMessageNotifyEnable() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_MESSAGE_NOTIFY_SWITCH, true);
    }

    public static void setMessageSoundEnable(boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_MESSAGE_SOUND_SWITCH, flag).apply();
    }


    public static boolean getMessageSoundEnable() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_MESSAGE_SOUND_SWITCH, true);
    }


    public static void setShakeSoundEnable(boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_SHAKE_SOUND_SWITCH, flag).apply();
    }

    public static boolean getShakeSoundEnable() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_SHAKE_SOUND_SWITCH, true);
    }

    public static String getCurrentRegion() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getString(KEY_CURRNET_REGION, null);
    }

    public static void setCurrentRegion(String region) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_CURRNET_REGION, region).apply();
    }

    public static void setMessageReceiptEnable(boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_MESSAGE_RECEIPT_SWITCH, flag).apply();
    }

    public static boolean getMessageReceiptEnable() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_MESSAGE_RECEIPT_SWITCH, true);
    }

    public static void setMessageStatusVisable(boolean flag) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_MESSAGE_STATUS_SWITCH, flag).apply();
    }

    public static boolean getMessageStatusVisable() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_MESSAGE_STATUS_SWITCH, true);
    }

    public static String getServerPath() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getString(KEY_SETTING_SERVER_PATH, Constant.SERVER_URL);
    }

    public static void setServerPath(String url) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_SETTING_SERVER_PATH, url).apply();
    }

    public static int getServerCIMPort() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getInt(KEY_SETTING_SERVER_PORT, Constant.CIM_SERVER_PORT);
    }

    public static void setServerCIMPort(int port) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_SETTING_SERVER_PORT, port).apply();
    }

    public static String getServerHost() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        String host = sp.getString(KEY_SETTING_SERVER_HOST, null);
        if (host == null) {
            try {
                host = new URL(Constant.SERVER_URL).getHost();
            } catch (MalformedURLException e) {
            }
        }
        return host;
    }

    public static void setServerHost(String url) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_SETTING_SERVER_HOST, url).apply();
    }

    public static int getKeybordHeight(final int defaultHeight) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getInt(KEY_SOFTKEYBORD_HEIGHT, defaultHeight);
    }

    public static void saveKeybordHeight(int height) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_SOFTKEYBORD_HEIGHT, height).apply();
    }

}
