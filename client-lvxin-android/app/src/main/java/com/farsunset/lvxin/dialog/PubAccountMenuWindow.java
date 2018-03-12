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
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.farsunset.lvxin.model.PublicMenu;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;

import java.util.ArrayList;
import java.util.List;

public class PubAccountMenuWindow extends PopupWindow implements OnItemClickListener {
    private List<PublicMenu> list = new ArrayList<PublicMenu>();
    private OnMenuClickListener clickListener;
    private Context mContext;
    private ListViewAdapter listViewAdapter;
    private int windowWidth;
    private int menuWidth;

    public PubAccountMenuWindow(Context context, OnMenuClickListener listener) {
        super(context, null);
        mContext = context;
        clickListener = listener;
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.abc_menu_hardkey_panel_mtrl_mult));
        ListView listView = (ListView) LayoutInflater.from(context).inflate(R.layout.layout_pub_menuwindow, null);
        listViewAdapter = new ListViewAdapter();
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(this);
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        windowWidth = (screenWidth - context.getResources().getDimensionPixelOffset(R.dimen.padding_10dip) * 6) / 2;
        menuWidth = (screenWidth - context.getResources().getDimensionPixelOffset(R.dimen.padding_10dip) * 6) / 3;
        setContentView(listView);
        setWidth(windowWidth);
        setHeight(LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
    }

    public void showAtLocation(View parent, List<PublicMenu> mlist) {

        list.clear();
        list.addAll(mlist);
        listViewAdapter.notifyDataSetChanged();
        int index = (Integer) parent.getTag(R.drawable.icon);
        int left = mContext.getResources().getDimensionPixelOffset(R.dimen.padding_10dip) * 6 + index * menuWidth + (menuWidth - windowWidth) / 2;
        int bottom = AppTools.dip2px(50f);
        super.showAtLocation(parent, Gravity.BOTTOM | Gravity.START, left, bottom);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        clickListener.onMenuClicked(listViewAdapter.getItem(position));
        dismiss();
    }

    public interface OnMenuClickListener {
        void onMenuClicked(PublicMenu menu);
    }

    public class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size() + 1;
        }

        @Override
        public PublicMenu getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == list.size()) {
                return new View(parent.getContext());
            }
            PublicMenu target = getItem(position);
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_textview, null);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(target.name);
            return convertView;
        }

    }

}
