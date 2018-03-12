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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.comparator.DistanceAscComparator;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;
import com.farsunset.lvxin.util.FileURLBuilder;

import java.util.Collections;
import java.util.List;

public class NearbyListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Friend> list;
    private User self;

    public NearbyListViewAdapter(Context c, List<Friend> list) {
        super();
        this.context = c;
        this.list = list;
        self = Global.getCurrentUser();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Friend getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void notifyDataSetChanged() {

        Collections.sort(list, new DistanceAscComparator(self));
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int index, View friendItemView, ViewGroup parent) {

        Friend target = getItem(index);

        ViewHolder viewHolder;
        if (friendItemView == null) {
            friendItemView = LayoutInflater.from(context).inflate(R.layout.item_nearby_user, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.icon = (WebImageView) friendItemView.findViewById(R.id.icon);
            viewHolder.name = (TextView) friendItemView.findViewById(R.id.name);
            viewHolder.distance = (TextView) friendItemView.findViewById(R.id.distance);
            viewHolder.location = (TextView) friendItemView.findViewById(R.id.location);
            friendItemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) friendItemView.getTag();
        }
        viewHolder.icon.load(FileURLBuilder.getUserIconUrl(target.account), R.drawable.icon_def_head);
        viewHolder.name.setText(target.name);

        if (self.longitude == null || self.latitude == null) {
            viewHolder.distance.setText(R.string.tip_unknow_distance);
        } else {
            viewHolder.distance.setText(AppTools.transformDistance(self.longitude, self.latitude, target.longitude, target.latitude));
        }
        if (target.location == null || "null".equals(target.location)) {
            viewHolder.location.setText(R.string.tip_unknow_location);
        } else {
            viewHolder.location.setText(target.location);
        }

        if (User.GENDER_FEMALE.equals(target.gender)) {

            Drawable image = ContextCompat.getDrawable(context, R.drawable.icon_lady);
            image.setBounds(0, 0, (int) (Resources.getSystem().getDisplayMetrics().density * 12), (int) (Resources.getSystem().getDisplayMetrics().density * 12));
            viewHolder.name.setCompoundDrawables(null, null, image, null);
        } else if (User.GENDER_MAN.equals(target.gender)) {
            Drawable image = ContextCompat.getDrawable(context, R.drawable.icon_man);
            image.setBounds(0, 0, (int) (Resources.getSystem().getDisplayMetrics().density * 12), (int) (Resources.getSystem().getDisplayMetrics().density * 12));
            viewHolder.name.setCompoundDrawables(null, null, image, null);

        } else {
            viewHolder.name.setCompoundDrawables(null, null, null, null);
        }
        return friendItemView;
    }


    private static class ViewHolder {
        TextView location;
        TextView distance;
        TextView name;
        WebImageView icon;
    }


}
