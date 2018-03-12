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

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.Bucket;
import com.farsunset.lvxin.pro.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumBucketLoader extends CursorLoader {
    public static final String COLUMN_COUNT = "count";
    private static final String[] COLUMNS = {MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media._ID, COLUMN_COUNT};
    private static final String[] PROJECTION = {MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media._ID, "COUNT(*) AS " + COLUMN_COUNT};
    private static final String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
    private static final String BUCKET_ORDER_BY = "MAX(" + MediaStore.Images.Media.DATE_TAKEN + ") DESC";
    private static final String MEDIA_ID_DUMMY = String.valueOf(-1);

    public AlbumBucketLoader(Context context) {
        super(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION, BUCKET_GROUP_BY, null,
                BUCKET_ORDER_BY);
    }

    @Override
    public Cursor loadInBackground() {
        Cursor albums = super.loadInBackground();
        MatrixCursor allAlbum = new MatrixCursor(COLUMNS);
        int totalCount = 0;
        while (albums.moveToNext()) {
            totalCount += albums.getInt(albums.getColumnIndex(COLUMN_COUNT));
        }
        String allAlbumId;
        if (albums.moveToFirst()) {
            allAlbumId = albums.getString(albums.getColumnIndex(MediaStore.Images.Media._ID));
        } else {
            allAlbumId = MEDIA_ID_DUMMY;
        }
        allAlbum.addRow(new String[]{null, LvxinApplication.getInstance().getString(R.string.label_ablbum_all), allAlbumId, String.valueOf(totalCount)});

        return new MergeCursor(new Cursor[]{allAlbum, albums});
    }

    public List<Bucket> syncLoadList() {
        Cursor cursor = loadInBackground();
        List<Bucket> list = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            Bucket bucket = new Bucket();
            bucket.id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
            bucket.name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            bucket.size = cursor.getLong(cursor.getColumnIndex(COLUMN_COUNT));
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            bucket.cover = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            list.add(bucket);
        }
        AppTools.closeQuietly(cursor);
        return list;
    }

    @Override
    public void onContentChanged() {
    }
}