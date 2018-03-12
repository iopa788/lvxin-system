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
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.farsunset.lvxin.activity.trend.FriendMomentActivity;
import com.farsunset.lvxin.activity.trend.SelfMomentActivity;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.component.ListFooterView;
import com.farsunset.lvxin.component.SNSMomentHeaderView;
import com.farsunset.lvxin.component.SnsItemMomentView;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.listener.OnCommentSelectedListener;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.StringUtils;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class MomentListViewAdapter extends RecyclerView.Adapter implements OnClickListener {
    protected int ACTION_HEADER = 0;
    protected int ACTION_ITEM_PHOTOS = 1;
    protected int ACTION_FOOTER = 9999;
    protected int ACTION_ITEM_LINK = 2;
    protected int ACTION_ITEM_TEXT = 3;
    protected int ACTION_ITEM_ONEPHOTO = 4;
    protected int ACTION_ITEM_VIDEO = 5;

    private List<Moment> list = new ArrayList<>();
    private OnCommentSelectedListener commentSelectedListener;
    private User self;
    private SNSMomentHeaderView mHeaderView;
    private ListFooterView mFooterView;

    public MomentListViewAdapter(Context context) {
        super();
        self = Global.getCurrentUser();
        mHeaderView = (SNSMomentHeaderView) LayoutInflater.from(context).inflate(R.layout.layout_circle_listheader, null);
        mFooterView = (ListFooterView) LayoutInflater.from(context).inflate(R.layout.layout_list_footer, null);
    }

    public Moment getItem(int position) {
        return list.get(position);
    }

    public void setOnCommentSelectedListener(OnCommentSelectedListener commentClickListener) {
        this.commentSelectedListener = commentClickListener;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ACTION_HEADER;
        }

        if (position == getItemCount() - 1) {
            return ACTION_FOOTER;
        }

        Moment moment = getItem(position - 1);
        if (moment.type.equals(Moment.FORMAT_LINK)) {
            return ACTION_ITEM_LINK;
        }
        if (moment.type.equals(Moment.FORMAT_VIDEO)) {
            return ACTION_ITEM_VIDEO;
        }
        if (moment.type.equals(Moment.FORMAT_TEXT_IMAGE) && StringUtils.isEmpty(moment.thumbnail)) {
            return ACTION_ITEM_TEXT;
        }

        int photoSize = new JsonParser().parse(moment.thumbnail).getAsJsonArray().size();
        if (photoSize == 1) {
            return ACTION_ITEM_ONEPHOTO;
        }

        return ACTION_ITEM_PHOTOS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ACTION_HEADER) {
            return new HeaderViewHolder(mHeaderView);
        }

        if (viewType == ACTION_FOOTER) {
            return new FooterViewHolder(mFooterView);
        }

        return getMomentItemViewHolder(parent, viewType);
    }

    public RecyclerView.ViewHolder getMomentItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == ACTION_ITEM_PHOTOS) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sns_moment_multi_photo, parent, false);
        }
        if (viewType == ACTION_ITEM_LINK) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sns_moment_link, parent, false);
        }
        if (viewType == ACTION_ITEM_ONEPHOTO) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sns_moment_one_photo, parent, false);
        }
        if (viewType == ACTION_ITEM_TEXT) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sns_moment_text, parent, false);
        }
        if (viewType == ACTION_ITEM_VIDEO) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sns_moment_video, parent, false);
        }
        return new MomentViewHolder(itemView);
    }


    public void onBindMomentViewHolder(RecyclerView.ViewHolder holder, final Moment target) {
        final MomentViewHolder viewHolder = (MomentViewHolder) holder;
        viewHolder.itemMomentView.displayMoment(target, self, commentSelectedListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position > 0 && position < getItemCount() - 1) {
            final int index = position - 1;
            final Moment target = getItem(index);
            onBindMomentViewHolder(holder, target);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.icon) {
            Intent intent = new Intent();
            if (self.account.equals(v.getTag(R.id.account).toString())) {
                intent.setClass(v.getContext(), SelfMomentActivity.class);
            } else {
                intent.setClass(v.getContext(), FriendMomentActivity.class);
                intent.putExtra(Friend.class.getName(), FriendRepository.queryFriend(v.getTag(R.id.account).toString()));
            }
            v.getContext().startActivity(intent);
        }
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean listEquals(List<Moment> list) {
        if (this.list.size() < list.size()) {
            return false;
        }

        return this.list.subList(0, list.size()).equals(list);
    }

    public void replaceFirstPage(List<Moment> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(List<Moment> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        int lastIndex = this.list.size();
        if (this.list.addAll(list)) {
            notifyItemRangeInserted(lastIndex + 1, list.size());
        }
    }

    public void add(Moment article) {
        list.add(0, article);
        notifyItemInserted(1);
    }

    public void remove(Moment article) {
        int index = indexOf(article.gid);
        if (index >= 0) {
            list.remove(index);
            notifyItemRemoved(index + 1);
        }
    }

    public void update(Moment article) {
        int index = indexOf(article.gid);
        if (index >= 0) {
            list.set(index, article);
            notifyItemChanged(index + 1);
        }
    }

    public int indexOf(String gid) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).gid.equals(gid)) {
                return i;
            }
        }
        return -1;
    }

    public SNSMomentHeaderView getHeaderView() {

        return mHeaderView;
    }

    public ListFooterView getFooterView() {

        return mFooterView;
    }

    protected class MomentViewHolder extends RecyclerView.ViewHolder {
        SnsItemMomentView itemMomentView;

        public MomentViewHolder(View itemView) {
            super(itemView);
            itemMomentView = (SnsItemMomentView) itemView;
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
