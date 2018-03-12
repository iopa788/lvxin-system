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
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.farsunset.lvxin.adapter.viewholder.ContactsViewHolder;
import com.farsunset.lvxin.adapter.viewholder.TextViewHolder;
import com.farsunset.lvxin.comparator.NameAscComparator;
import com.farsunset.lvxin.listener.OnContactHandleListener;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.CharacterParser;
import com.farsunset.lvxin.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactSelectorAdapter extends RecyclerView.Adapter implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private OnContactHandleListener onContactHandleListener;
    private ArrayList<Object> contentList = new ArrayList<>();
    private ArrayList<Friend> selectedList = new ArrayList<>();
    protected static final int TYPE_CHAR = 1;
    protected static final int TYPE_CONTACTS = 2;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_CHAR)
        {
            return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_contact_char, parent, false));
        }
        if (viewType == TYPE_CONTACTS)
        {
            return new ContactsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_selector, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof ContactsViewHolder)
        {
            Friend friend = (Friend) contentList.get(position);
            ContactsViewHolder contactsHolder = (ContactsViewHolder) holder;
            contactsHolder.icon.load(FileURLBuilder.getUserIconUrl(friend.account), R.drawable.icon_def_head);
            contactsHolder.name.setText(friend.name);
            contactsHolder.itemView.setTag(friend);
            contactsHolder.itemView.setOnClickListener(this);

            contactsHolder.checkBox.setOnCheckedChangeListener(null);
            contactsHolder.checkBox.setChecked(selectedList.contains(friend));
            contactsHolder.checkBox.setOnCheckedChangeListener(this);
            contactsHolder.checkBox.setTag(friend);

            return;
        }

        if (holder instanceof TextViewHolder && contentList.get(position) instanceof Character)
        {
            TextViewHolder textViewHolder = (TextViewHolder) holder;
            textViewHolder.textView.setText(contentList.get(position).toString());
            return;
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (contentList.get(position) instanceof  Friend)
        {
            return TYPE_CONTACTS;
        }else {
            return TYPE_CHAR;
        }
    }

    public void notifyDataSetChanged(List<Friend> friendList) {
        this.contentList.clear();
        buildContentList(friendList);
        super.notifyDataSetChanged();
    }

    public ArrayList<Friend> getSelectedList() {
        return selectedList;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public boolean isEmpty() {
        return contentList.isEmpty();
    }


    @Override
    public void onClick(View v) {
        CheckBox checkbox = ((CheckBox) v.findViewById(R.id.checkbox));
        checkbox.setChecked(!checkbox.isChecked());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            selectedList.add((Friend) buttonView.getTag());
            onContactHandleListener.onContactSelected((Friend) buttonView.getTag());
        } else {
            selectedList.remove(buttonView.getTag());
            onContactHandleListener.onContactCanceled((Friend) buttonView.getTag());
        }
    }

    public void setOnContactHandleListener(OnContactHandleListener onContactHandleListener) {
        this.onContactHandleListener = onContactHandleListener;
    }

    private void buildContentList(List<Friend> friendList) {

        for (Friend friend : friendList) {
            // 汉字转换成拼音
            friend.fristChar = CharacterParser.getFirstPinYinChar(friend.name);
        }

        Collections.sort(friendList, new NameAscComparator());

        for (Friend friend : friendList) {
            if (!contentList.contains(friend.fristChar))
            {
                contentList.add(friend.fristChar);
            }
            contentList.add(friend);
        }
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(char section) {
        return contentList.indexOf(section);
    }

}
