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
package com.farsunset.lvxin.activity.contact;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farsunset.lvxin.activity.base.CIMMonitorFragment;
import com.farsunset.lvxin.adapter.ContactsListViewAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.component.CharSelectorBar;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.listener.OnTouchMoveCharListener;
import com.farsunset.lvxin.network.ContactsRequester;
import com.farsunset.lvxin.network.MomentRuleRequester;
import com.farsunset.lvxin.network.PullOfflineMessageRequester;
import com.farsunset.lvxin.network.SQLiteDatabaseRequester;
import com.farsunset.lvxin.pro.R;

public class ContactsFragment extends CIMMonitorFragment implements  OnTouchMoveCharListener {

    private RecyclerView recyclerView;
    private ContactsChangedReceiver contactsChangedReceiver;
    private ContactsListViewAdapter adapter;
    private LinearLayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        CharSelectorBar sideBar = (CharSelectorBar) findViewById(R.id.sidrbar);
        sideBar.setTextView((TextView) findViewById(R.id.dialog));
        sideBar.setOnTouchMoveCharListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager = new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter = new ContactsListViewAdapter());
        adapter.notifyDataSetChanged(FriendRepository.queryFriendList());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        SQLiteDatabaseRequester.execute();
        ContactsRequester.execute();
        MomentRuleRequester.execute();

        contactsChangedReceiver = new ContactsChangedReceiver();
        LvxinApplication.registerLocalReceiver(contactsChangedReceiver, contactsChangedReceiver.getIntentFilter());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LvxinApplication.unregisterLocalReceiver(contactsChangedReceiver);
    }

    @Override
    public void onCharChanged(char s) {
        // 该字母首次出现的位置
        int position = adapter.getPositionForSection(s);
        if (position != -1 ) {
            layoutManager.scrollToPositionWithOffset(position+1,0);
            layoutManager.setStackFromEnd(true);
        }
    }

    public class ContactsChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter.notifyDataSetChanged(FriendRepository.queryFriendList());
            /**
             *如果用户是首次登录进来，先获取通讯录之后再拉取离线消息
             */
            if (!Global.getAlreadyLogin(Global.getCurrentAccount()))
            {
                PullOfflineMessageRequester.pull();
                Global.saveAlreadyLogin(Global.getCurrentAccount());
            }
        }

        public IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_RELOAD_CONTACTS);
            return filter;
        }

    }
}
