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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.database.ConfigRepository;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.pro.BuildConfig;
import com.tencent.bugly.crashreport.CrashReport;

public class Global {

    static final String MODEL_KEY = "CURRENT_USER";

    static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";

    static final String KEY_USER_ACCOUNT = "KEY_USER_ACCOUNT";
    static final String KEY_USER_NAME = "KEY_USER_NAME";
    static final String KEY_USER_MOTTO = "KEY_USER_MOTTO";
    static final String KEY_USER_LONGITUDE = "KEY_USER_LONGITUDE";
    static final String KEY_USER_LATITUDE = "KEY_USER_LATITUDE";
    static final String KEY_USER_LOCATION = "KEY_USER_LOCATION";
    static final String KEY_USER_GENDER = "KEY_USER_GENDER";

    static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    static final String KEY_USER_TELEPHONE = "KEY_USER_TELEPHONE";
    static final String KEY_USER_ORGCODE = "KEY_USER_ORGCODE";

    static final String KEY_PHOTO_GRAPH_FILE_PATH = "KEY_PHOTO_GRAPH_FILE_PATH";
    static final String KEY_CROP_PHOTO_FILE_PATH = "KEY_CROP_PHOTO_FILE_PATH";

    static final String KEY_IGNORE_GROUP_MESSAGE = "IGNORE_GROUP_MESSAGE_%1$s";

    static final String KEY_CHAT_DRAFT = "KEY_CHAT_DRAFT_%1$s_%2$s";

    static final String KEY_APP_INBACGROUND = "KEY_APP_INBACGROUND";

    static final String KEY_APP_TOP_ACTIVITY = "KEY_APP_TOP_ACTIVITY";

    static final String KEY_BETTERY_SAVING_SHOW = "KEY_BETTERY_SAVING_SHOW";

    static final String KEY_CHATING_TEXTVIEW_WIDTH = "KEY_CHATING_TEXTVIEW_WIDTH";

    static final String KEY_FRIST_LOGIN = "KEY_FRIST_LOGIN_%1$s";

    private static User mUser;

    private Global() {
    }

    public static User getCurrentUser() {

        if (mUser != null) {
            return mUser;
        }
        Account account = getLoginedAccount();
        if (account != null) {
            AccountManager accountManager = AccountManager.get(LvxinApplication.getInstance());
            mUser = new User();
            mUser.account = accountManager.getUserData(account, KEY_USER_ACCOUNT);
            mUser.password = accountManager.getPassword(account);
            mUser.name = accountManager.getUserData(account, KEY_USER_NAME);
            mUser.gender = accountManager.getUserData(account, KEY_USER_GENDER);
            mUser.motto = accountManager.getUserData(account, KEY_USER_MOTTO);

            String longitude = accountManager.getUserData(account, KEY_USER_LONGITUDE);
            if (!TextUtils.isEmpty(longitude)) {
                mUser.longitude = Double.parseDouble(longitude);
            }
            String latitude = accountManager.getUserData(account, KEY_USER_LATITUDE);
            if (!TextUtils.isEmpty(latitude)) {
                mUser.latitude = Double.parseDouble(latitude);
            }

            mUser.location = accountManager.getUserData(account, KEY_USER_LOCATION);
            mUser.email = accountManager.getUserData(account, KEY_USER_EMAIL);
            mUser.telephone = accountManager.getUserData(account, KEY_USER_TELEPHONE);
            mUser.orgCode = accountManager.getUserData(account, KEY_USER_ORGCODE);

            return mUser;
        }

        return null;
    }

    public static void addAccount(User user) {
        if (user != null) {
            CrashReport.setUserId(user.account);
            mUser = user;
            AccountManager accountManager = AccountManager.get(LvxinApplication.getInstance());
            Account account = new Account(user.name, LvxinApplication.getInstance().getPackageName());

            Bundle bundle = new Bundle();
            bundle.putString(KEY_USER_ACCOUNT, user.account);
            bundle.putString(KEY_USER_NAME, user.name);
            bundle.putString(KEY_USER_GENDER, user.gender);
            bundle.putString(KEY_USER_MOTTO, user.motto);
            bundle.putString(KEY_USER_LONGITUDE, user.longitude == null ? null : user.longitude.toString());
            bundle.putString(KEY_USER_LATITUDE, user.latitude == null ? null : user.latitude.toString());
            bundle.putString(KEY_USER_LOCATION, user.location);

            bundle.putString(KEY_USER_EMAIL, user.email);
            bundle.putString(KEY_USER_TELEPHONE, user.telephone);
            bundle.putString(KEY_USER_ORGCODE, user.orgCode);

            accountManager.addAccountExplicitly(account, user.password, bundle);
        }
    }

    public static void modifyAccount(User user) {
        AccountManager accountManager = AccountManager.get(LvxinApplication.getInstance());
        Account account = getLoginedAccount();
        if (account != null) {

            mUser = user;

            accountManager.setUserData(account, KEY_USER_NAME, user.name);
            accountManager.setUserData(account, KEY_USER_GENDER, user.gender);
            accountManager.setUserData(account, KEY_USER_MOTTO, user.motto);
            accountManager.setUserData(account, KEY_USER_LONGITUDE, user.longitude == null ? null : user.longitude.toString());
            accountManager.setUserData(account, KEY_USER_LATITUDE, user.latitude == null ? null : user.latitude.toString());
            accountManager.setUserData(account, KEY_USER_LOCATION, user.location);
            accountManager.setPassword(account, user.password);
        }

    }

    public static void removeAccount(AccountManagerCallback callback) {
        AccountManager accountManager = AccountManager.get(LvxinApplication.getInstance());
        Account account = getLoginedAccount();
        if (account != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                accountManager.removeAccountExplicitly(account);
                callback.run(null);
            } else {
                accountManager.removeAccount(account, callback, null);
            }
        } else {
            callback.run(null);
        }

        mUser = null;
    }

    public static void removePassword() {
        AccountManager accountManager = AccountManager.get(LvxinApplication.getInstance());
        Account account = getLoginedAccount();
        if (account != null) {
            accountManager.clearPassword(account);
        }

        mUser = null;
    }


    private static Account getLoginedAccount() {
        AccountManager accountManager = AccountManager.get(LvxinApplication.getInstance());

        Account[] accounts = accountManager.getAccountsByType(BuildConfig.APPLICATION_ID);
        if (accounts != null && accounts.length > 0) {
            return accounts[0];
        }
        return null;
    }

    /**
     * 获取当前账号
     */
    public static String getCurrentAccount() {

        User user = getCurrentUser();
        if (user != null) {
            return user.account;
        }
        return null;
    }

    public static int getChatTextMaxWidth() {
        return LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE)
                .getInt(KEY_CHATING_TEXTVIEW_WIDTH,0);
    }

    public static void setChatTextMaxWidth(int w) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_CHATING_TEXTVIEW_WIDTH, w).apply();
    }
    /**
     * 获取拍照的照片文件地址
     *
     * @return
     */
    public static String getPhotoGraphFilePath() {
        return LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE)
                .getString(KEY_PHOTO_GRAPH_FILE_PATH, null);
    }

    /**
     * 设置拍照的照片文件地址
     */
    public static void setPhotoGraphFilePath(String path) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_PHOTO_GRAPH_FILE_PATH, path).apply();
    }

    /**
     * 获取裁剪图片文件地址
     *
     * @return
     */
    public static String getCropPhotoFilePath() {
        return LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE)
                .getString(KEY_CROP_PHOTO_FILE_PATH, null);
    }

    /**
     * 设置裁剪图片文件地址
     */
    public static void setCropPhotoFilePath(String path) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_CROP_PHOTO_FILE_PATH, path).apply();
    }

    /**
     * 设置拍照的照片文件地址
     */
    public static void saveIgnoredGroupMessage(String groupId, boolean isIgnored) {

        if (isIgnored) {
            ConfigRepository.add(String.format(KEY_IGNORE_GROUP_MESSAGE, groupId), "1");
        } else {
            ConfigRepository.delete(String.format(KEY_IGNORE_GROUP_MESSAGE, groupId));
        }

    }

    /**
     * 是否屏蔽了某个群消息
     *
     * @return
     */
    public static boolean getIsIgnoredGroupMessage(String groupId) {
        String value = ConfigRepository.queryValue(String.format(KEY_IGNORE_GROUP_MESSAGE, groupId));
        return value != null && "1".equals(value);
    }


    /**
     * 保存聊天草稿
     */
    public static void saveChatDraft(MessageSource source, String text) {

        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(String.format(KEY_CHAT_DRAFT, source.getId(), source.getSourceType()), text).apply();

    }

    /**
     * 获取聊天草稿
     *
     * @return
     */
    public static String getLastChatDraft(MessageSource source) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getString(String.format(KEY_CHAT_DRAFT, source.getId(), source.getSourceType()), null);
    }

    /**
     * 删除聊天草稿
     */
    public static void removeChatDraft(MessageSource source) {

        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().remove(String.format(KEY_CHAT_DRAFT, source.getId(), source.getSourceType())).apply();

    }


    /**
     * 保存最上层activity名字
     */
    public static void saveTopActivityClassName(Class nameClass) {

        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_APP_TOP_ACTIVITY, nameClass.getName()).apply();

    }

    /**
     * 获取最上层activity名字
     *
     * @return
     */
    public static String getTopActivityClassName() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getString(KEY_APP_TOP_ACTIVITY, null);
    }

    /**
     * 保存应用是否切换到后台
     */
    public static void saveAppInBackground(boolean flag) {

        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_APP_INBACGROUND, flag).apply();

    }

    /**
     * 获取应用是否切换到后台
     *
     * @return
     */
    public static boolean getAppInBackground() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_APP_INBACGROUND, true);
    }

    /**
     * 设置用户是否第一次登录进来为true
     */
    public static void saveAlreadyLogin(String account) {

        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(String.format(KEY_FRIST_LOGIN,account), true).apply();

    }

    /**
     * 获取用户是否第一次登录进来
     */
    public static boolean getAlreadyLogin(String account) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(String.format(KEY_FRIST_LOGIN,account), false);
    }

    public static String getAccessToken() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getString(KEY_ACCESS_TOKEN, null);
    }

    public static void saveAccessToken(String token) {

        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_ACCESS_TOKEN, token).apply();
    }

    /**
     * 省电提示是否显示过
     *
     * @return
     */
    public static boolean getBetterySavingHasShow() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_BETTERY_SAVING_SHOW, false);
    }

    /**
     * 省电提示显示过
     *
     * @return
     */
    public static void setBetterySavingHasShow() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_BETTERY_SAVING_SHOW, true).apply();
    }
}
