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
package com.farsunset.lvxin.dialog;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.farsunset.lvxin.bean.Bucket;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.pro.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumBucketWindow extends BottomSheetDialog implements OnItemClickListener {
    private List<Bucket> list = new ArrayList<Bucket>();
    private OnItemClickedListener clickListener;
    private ListView listView;
    private Bucket mTarget;
    public AlbumBucketWindow(Context context, OnItemClickedListener listener) {
        super(context);
        clickListener = listener;
        listView = (ListView) LayoutInflater.from(context).inflate(R.layout.layout_pub_menuwindow, null);
        listView.setOnItemClickListener(this);
        listView.setAdapter(new ListViewAdapter());
        setContentView(listView);
    }
    public void setAlbumBucketList(List<Bucket> list) {
        if (!list.isEmpty())
        {
            mTarget = list.get(0);
        }
        this.list = list;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        clickListener.onItemClicked(mTarget =list.get(position), view);
        dismiss();
    }

    public class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Bucket getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Bucket target = getItem(position);
            if (convertView==null)
            {
                convertView = View.inflate(parent.getContext(), R.layout.item_change_folder, null);
            }
            TextView name = (TextView) convertView.findViewById(R.id.name);
            WebImageView image = ((WebImageView) convertView.findViewById(R.id.image));
            image.load(target.cover, R.color.theme_window_color);
            name.setText(getContext().getString(R.string.label_album_name_count,target.name,target.size));
            if (target == mTarget)
            {
                convertView.findViewById(R.id.mark).setVisibility(View.VISIBLE);
            }
            return convertView;
        }

    }

}
