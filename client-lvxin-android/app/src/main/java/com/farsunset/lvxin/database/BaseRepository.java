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


import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.listener.DatabaseChangedListener;
import com.farsunset.lvxin.util.MD5;
import com.j256.ormlite.dao.Dao;

import org.apache.commons.io.IOUtils;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepository<T, ID> implements DatabaseChangedListener {

    protected GlobalDatabaseHelper mDatabaseHelper;
    protected Dao<T, ID> databaseDao;

    public BaseRepository() {
        GlobalDatabaseHelper.registerListener(this);
        create();
    }

    private void create(){
        mDatabaseHelper = GlobalDatabaseHelper.createDatabaseHelper(getDatabaseName());
        try {
            Class  rawType = (Class)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            databaseDao = (Dao<T, ID>) mDatabaseHelper.getDao(rawType);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDatabaseChanged(){
        create();
    }

    public void onTableClearAll(){
        clearTable();
    }

    public void clearTable(){
        clearAll();
    }
    @Override
    public void onDatabaseDestroy(){
        destroy();
    }
    public String getDatabaseName() {
        return MD5.digest(Global.getCurrentAccount()) + ".db";
    }

    public void innerSave(T obj) {
        try {
            databaseDao.create(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void innerSaveAll(List<T> list) {
        try {
            databaseDao.create(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveIfNotExists(T obj) {
        try {
            databaseDao.createIfNotExists(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<T> innerQueryAll() {
        List<T> list = new ArrayList<T>();
        try {
            list = databaseDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void innerUpdate(T obj) {
        try {
            databaseDao.update(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearAll() {
        try {
            databaseDao.deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createOrUpdate(T obj) {
        try {
            databaseDao.createOrUpdate(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public T innerQueryById(ID gid) {
        try {
            return databaseDao.queryForId(gid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void innerExecuteSQL(String SQL, String[] strings) {

        try {
            databaseDao.executeRaw(SQL, strings);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void innerExecuteSQL(String SQL) {
        try {
            databaseDao.executeRawNoArgs(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void innerDeleteById(ID gid) {
        try {
            databaseDao.deleteById(gid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void innerDelete(T obj) {
        try {
            databaseDao.delete(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        mDatabaseHelper.close();
        mDatabaseHelper = null;
        databaseDao.clearObjectCache();
        IOUtils.closeQuietly(databaseDao.getConnectionSource());
    }

}
