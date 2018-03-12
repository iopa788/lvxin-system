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
package com.farsunset.lvxin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.model.ShakeRecord;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;
import com.farsunset.lvxin.util.FileURLBuilder;

import java.util.List;

public class ShakeRecordListViewAdapter extends BaseAdapter {

    private User self;
    private List<ShakeRecord> list;

    public ShakeRecordListViewAdapter(List<ShakeRecord> list) {
        super();
        this.list = list;
        self = Global.getCurrentUser();
    }

    public void remove(ShakeRecord target) {
        list.remove(target);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public ShakeRecord getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int index, View itemView, ViewGroup parent) {

        ShakeRecord target = getItem(index);

        if (itemView == null) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shake_record, null);
        }

        ((TextView) itemView.findViewById(R.id.username)).setText(target.name);
        if (target.longitude != null || target.latitude != null) {
            ((TextView) itemView.findViewById(R.id.location)).setText(parent.getContext().getString(R.string.label_distance_of, AppTools.transformDistance(self.longitude, self.latitude, target.longitude, target.latitude)));
        }

        if (User.GENDER_FEMALE.equals(target.gender)) {
            ((ImageView) itemView.findViewById(R.id.gender)).setImageResource(R.drawable.icon_lady);
            itemView.findViewById(R.id.gender).setVisibility(View.VISIBLE);
        }
        if (User.GENDER_MAN.equals(target.gender)) {
            ((ImageView) itemView.findViewById(R.id.gender)).setImageResource(R.drawable.icon_man);
            itemView.findViewById(R.id.gender).setVisibility(View.VISIBLE);
        }
        ((WebImageView) itemView.findViewById(R.id.icon)).load(FileURLBuilder.getUserIconUrl(target.account), R.drawable.icon_def_head);
        return itemView;
    }

}
