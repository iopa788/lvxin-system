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

import android.database.sqlite.SQLiteDatabase;

import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.listener.DatabaseChangedListener;
import com.farsunset.lvxin.model.Bottle;
import com.farsunset.lvxin.model.ChatTop;
import com.farsunset.lvxin.model.Comment;
import com.farsunset.lvxin.model.Config;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.GlideImage;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.GroupMember;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.model.MomentRule;
import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.model.PublicMenu;
import com.farsunset.lvxin.model.ShakeRecord;
import com.farsunset.lvxin.model.StarMark;
import com.farsunset.lvxin.pro.BuildConfig;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashSet;

public class GlobalDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private final static HashSet<DatabaseChangedListener> DATABASE_LISTENER_SET = new HashSet();
    private static GlobalDatabaseHelper instance;

    public GlobalDatabaseHelper(String databaseName) {
        super(LvxinApplication.getInstance(), databaseName, null, BuildConfig.VERSION_CODE);
    }

    public static synchronized GlobalDatabaseHelper createDatabaseHelper(String database) {
        if (instance == null || !instance.isOpen() || !database.equals(instance.getDatabaseName())) {
            instance = new GlobalDatabaseHelper(database);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource source) {

        try {
            if (getDatabaseName().equals(GlideImageRepository.DATABASE_NAME)) {
                TableUtils.createTableIfNotExists(source, GlideImage.class);
                return;
            }
            TableUtils.createTableIfNotExists(source, Message.class);
            TableUtils.createTableIfNotExists(source, Friend.class);
            TableUtils.createTableIfNotExists(source, Moment.class);
            TableUtils.createTableIfNotExists(source, Group.class);
            TableUtils.createTableIfNotExists(source, GroupMember.class);
            TableUtils.createTableIfNotExists(source, PublicAccount.class);
            TableUtils.createTableIfNotExists(source, PublicMenu.class);
            TableUtils.createTableIfNotExists(source, Config.class);
            TableUtils.createTableIfNotExists(source, ShakeRecord.class);
            TableUtils.createTableIfNotExists(source, Bottle.class);
            TableUtils.createTableIfNotExists(source, ChatTop.class);
            TableUtils.createTableIfNotExists(source, Comment.class);
            TableUtils.createTableIfNotExists(source, Organization.class);
            TableUtils.createTableIfNotExists(source, MomentRule.class);
            TableUtils.createTableIfNotExists(source, StarMark.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource source, int oldVersion, int newVersion) {
        onCreate(db, source);
    }



    public static void registerListener(DatabaseChangedListener listener) {
        DATABASE_LISTENER_SET.add(listener);
    }
    public static void unregisterListener(DatabaseChangedListener listener) {
        DATABASE_LISTENER_SET.remove(listener);
    }

    public static void onDatabaseDestroy() {
         for (DatabaseChangedListener listener : DATABASE_LISTENER_SET)
         {
             listener.onDatabaseDestroy();
         }
        DATABASE_LISTENER_SET.clear();
    }
    public static void onServerChanged() {
        for (DatabaseChangedListener listener : DATABASE_LISTENER_SET)
        {
            listener.onTableClearAll();
        }
    }
    public static void onAccountChanged() {
        for (DatabaseChangedListener listener : DATABASE_LISTENER_SET)
        {
            listener.onDatabaseChanged();
        }
    }
}
