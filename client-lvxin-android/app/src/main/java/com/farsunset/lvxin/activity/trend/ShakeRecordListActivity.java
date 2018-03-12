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
package com.farsunset.lvxin.activity.trend;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.activity.contact.UserDetailActivity;
import com.farsunset.lvxin.adapter.ShakeRecordListViewAdapter;
import com.farsunset.lvxin.database.ShakeRecordRepository;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.ShakeRecord;
import com.farsunset.lvxin.pro.R;

public class ShakeRecordListActivity extends BaseActivity implements OnDialogButtonClickListener, OnItemLongClickListener, OnItemClickListener {

    private ShakeRecordListViewAdapter adapter;
    private CustomDialog customDialog;

    @Override
    public void initComponents() {

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new ShakeRecordListViewAdapter(ShakeRecordRepository.queryRecordList());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        customDialog = new CustomDialog(this);
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setTitle(R.string.common_hint);
        customDialog.setMessage((R.string.tip_delete_record));

        findViewById(R.id.emptyView).setVisibility(adapter.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {

        ShakeRecord target = adapter.getItem(index);
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra(Friend.class.getName(), target.toFriend());
        this.startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int index, long arg3) {

        customDialog.setTag(adapter.getItem(index));
        customDialog.show();
        return true;
    }

    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        ShakeRecord target = (ShakeRecord) customDialog.getTag();
        adapter.remove(target);
        ShakeRecordRepository.deleteShakeRecord(target.account);
        customDialog.dismiss();

    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_common_listview;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_shake_record;
    }

}
