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

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.database.OrganizationRepository;
import com.farsunset.lvxin.listener.OSSFileDownloadListener;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Organization;
import com.farsunset.lvxin.util.AppTools;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseRequester {
    private static final String QUERY_ORG_SQL = "select * from " + Organization.TABLE_NAME;
    private static final String QUERY_USER_SQL = "select * from " + Friend.TABLE_NAME;

    public static void execute() {
        CloudFileDownloader.asyncDownloadDatabase(new OSSFileDownloadListener() {
            @Override
            public void onDownloadCompleted(final File file, String currentKey) {
                SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(file, null);
                Cursor cursor = database.rawQuery(QUERY_USER_SQL, null);
                if (cursor.moveToFirst()) {
                    List<Friend> list = new ArrayList<Friend>();
                    do {
                        Friend friend = new Friend();
                        friend.account = cursor.getString(cursor.getColumnIndex("account"));
                        friend.name = cursor.getString(cursor.getColumnIndex("name"));
                        friend.orgCode = cursor.getString(cursor.getColumnIndex("orgCode"));
                        friend.telephone = cursor.getString(cursor.getColumnIndex("telephone"));
                        friend.email = cursor.getString(cursor.getColumnIndex("email"));
                        friend.gender = cursor.getString(cursor.getColumnIndex("gender"));
                        friend.motto = cursor.getString(cursor.getColumnIndex("motto"));
                        list.add(friend);
                    } while (cursor.moveToNext());

                    FriendRepository.saveAll(list);
                }

                AppTools.closeQuietly(cursor);

                LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));

                cursor = database.rawQuery(QUERY_ORG_SQL, null);
                if (cursor.moveToFirst()) {
                    List<Organization> list = new ArrayList<Organization>();
                    do {
                        Organization org = new Organization();
                        org.code = cursor.getString(cursor.getColumnIndex("code"));
                        org.name = cursor.getString(cursor.getColumnIndex("name"));
                        org.parentCode = cursor.getString(cursor.getColumnIndex("parentCode"));
                        if (TextUtils.isEmpty(org.parentCode)) {
                            org.parentCode = null;
                        }
                        org.sort = cursor.getInt(cursor.getColumnIndex("sort"));
                        list.add(org);
                    } while (cursor.moveToNext());

                    OrganizationRepository.saveAll(list);
                }
                AppTools.closeQuietly(cursor);
                IOUtils.closeQuietly(database);
            }


            @Override
            public void onDownloadFailured(File file, String currentKey) {

            }

            @Override
            public void onDownloadProgress(String key, float progress) {
            }
        });
    }


}
