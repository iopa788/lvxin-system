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

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farsunset.lvxin.activity.chat.FriendChatActivity;
import com.farsunset.lvxin.activity.contact.GroupListActivity;
import com.farsunset.lvxin.activity.contact.OrganizationActivity;
import com.farsunset.lvxin.activity.contact.PubAccountListActivity;
import com.farsunset.lvxin.adapter.viewholder.ContactsHeaderHolder;
import com.farsunset.lvxin.adapter.viewholder.ContactsViewHolder;
import com.farsunset.lvxin.adapter.viewholder.TextViewHolder;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.comparator.NameAscComparator;
import com.farsunset.lvxin.component.CharSelectorBar;
import com.farsunset.lvxin.database.StarMarkRepository;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.CharacterParser;
import com.farsunset.lvxin.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactsListViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    protected static final int TYPE_HEADER = 0;
    protected static final int TYPE_CHAR = 1;
    protected static final int TYPE_CONTACTS = 2;
    protected static final int TYPE_FOOTER = 3;
    private List<Object> contentList = new ArrayList<>();
    private List<String> mStarMarkList = new ArrayList<>();
    protected int contactsSize;

    public void notifyDataSetChanged(List<Friend> friendList) {
        contactsSize = friendList.size();
        this.contentList.clear();
        mStarMarkList.clear();
        mStarMarkList.addAll(StarMarkRepository.queryAccountList());
        buildContentList(friendList);
        super.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER)
        {
            return new ContactsHeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_contact_header, parent, false));
        }
        if (viewType == TYPE_FOOTER)
        {
            return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_contact_footer, parent, false));
        }
        if (viewType == TYPE_CHAR)
        {
            return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_contact_char, parent, false));
        }
        if (viewType == TYPE_CONTACTS)
        {
            return new ContactsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_contact_friend, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContactsHeaderHolder)
        {
            ContactsHeaderHolder headerHolder = (ContactsHeaderHolder) holder;
            headerHolder.accountBar.setOnClickListener(this);
            headerHolder.accountBar.setTag(R.id.accountBar);
            headerHolder.groupBar.setOnClickListener(this);
            headerHolder.groupBar.setTag(R.id.groupBar);
            headerHolder.orgBar.setOnClickListener(this);
            headerHolder.orgBar.setTag(R.id.orgBar);
            return;
        }

        if (holder instanceof TextViewHolder && position == getItemCount() - 1)
        {
            TextViewHolder textHolder = (TextViewHolder) holder;
            textHolder.textView.setText(textHolder.textView.getContext().getString(R.string.label_contacts_frend_count,contactsSize));
            return;
        }

        if (holder instanceof ContactsViewHolder)
        {
            Friend friend = (Friend) contentList.get(position -1);
            ContactsViewHolder contactsHolder = (ContactsViewHolder) holder;
            contactsHolder.icon.load(FileURLBuilder.getUserIconUrl(friend.account), R.drawable.icon_def_head);
            contactsHolder.name.setText(friend.name);
            contactsHolder.itemView.setTag(friend);
            contactsHolder.itemView.setOnClickListener(this);
            return;
        }

        if (holder instanceof TextViewHolder && contentList.get(position-1) instanceof Character)
        {
            TextViewHolder textViewHolder = (TextViewHolder) holder;
            textViewHolder.textView.setText(contentList.get(position-1).toString());
            return;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_HEADER;
        }
        if(position == getItemCount() -1){
            return TYPE_FOOTER;
        }
        if (contentList.get(position -1) instanceof  Friend)
        {
            return TYPE_CONTACTS;
        }else {
            return TYPE_CHAR;
        }
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return contentList.size() + 2;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(char section) {
        return contentList.indexOf(section);
    }


    private void buildContentList(List<Friend> friendList) {

        List<Friend> topList = new ArrayList<>();
        for (Friend friend : friendList) {
            // 汉字转换成拼音
            friend.fristChar = CharacterParser.getFirstPinYinChar(friend.name);

            if (mStarMarkList.contains(friend.account)) {
                friend = friend.clone();
                friend.fristChar = CharSelectorBar.STAR;
                topList.add(friend);
            }
        }

        friendList.addAll(topList);
        Collections.sort(friendList, new NameAscComparator());

        for (Friend friend : friendList) {
            if (!contentList.contains(friend.fristChar))
            {
                contentList.add(friend.fristChar);
            }
            contentList.add(friend);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals( R.id.accountBar))
        {
            v.getContext().startActivity(new Intent(v.getContext(), PubAccountListActivity.class));
        }
        if (v.getTag().equals( R.id.groupBar))
        {
            v.getContext().startActivity(new Intent(v.getContext(), GroupListActivity.class));
        }
        if (v.getTag().equals( R.id.orgBar))
        {
            v.getContext().startActivity(new Intent(v.getContext(), OrganizationActivity.class));
        }
        if (v.getTag() instanceof Friend)
        {
            Friend friend = (Friend) v.getTag();
            Intent intent = new Intent(v.getContext(), FriendChatActivity.class);
            intent.putExtra(Constant.CHAT_OTHRES_ID, friend.account);
            intent.putExtra(Constant.CHAT_OTHRES_NAME, friend.name);
            v.getContext().startActivity(intent);
        }
    }
}
