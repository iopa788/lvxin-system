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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.farsunset.lvxin.adapter.viewholder.AppSettingViewHolder;
import com.farsunset.lvxin.app.ClientConfig;
import com.farsunset.lvxin.bean.AppSetting;
import com.farsunset.lvxin.pro.R;

import java.util.List;

public class AppSettingListAdapter extends RecyclerView.Adapter<AppSettingViewHolder> implements CompoundButton.OnCheckedChangeListener {

    private List<AppSetting> list ;

    public AppSettingListAdapter(List<AppSetting> list){
        this.list = list;
    }
    @Override
    public AppSettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppSettingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_setting, parent, false));
    }

    @Override
    public void onBindViewHolder(AppSettingViewHolder viewHolder, int position) {
        AppSetting setting = list.get(position);
        viewHolder.hint.setText(setting.hint);
        viewHolder.label.setText(setting.label);
        viewHolder.appSwitch.setTag(setting.key);
        viewHolder.appSwitch.setOnCheckedChangeListener(null);
        viewHolder.appSwitch.setChecked(getSettingSwitch(setting));
        viewHolder.appSwitch.setOnCheckedChangeListener(this);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean flag) {
        Object tag = arg0.getTag();
        ClientConfig.setBooleanConfig(tag.toString(),flag);
    }

    public boolean getSettingSwitch(AppSetting setting) {
        return  ClientConfig.getBooleanConfig(setting.key,Boolean.valueOf(setting.def));
    }
}
