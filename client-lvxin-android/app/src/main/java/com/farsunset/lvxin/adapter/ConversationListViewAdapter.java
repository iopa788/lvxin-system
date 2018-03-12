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

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farsunset.lvxin.adapter.viewholder.RecentChatViewHolder;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.comparator.TimeDescComparator;
import com.farsunset.lvxin.database.ChatTopRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.listener.OnChatingHandlerListener;
import com.farsunset.lvxin.message.parser.MessageParserFactory;
import com.farsunset.lvxin.model.ChatTop;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

//最近对话列表
public class ConversationListViewAdapter extends RecyclerView.Adapter<RecentChatViewHolder> implements Comparator<ChatItem>, View.OnLongClickListener, View.OnClickListener {
    private OnChatingHandlerListener onChatItemHandleListner;
    private List<ChatItem> dataList = new ArrayList<>();
    private List<ChatTop> chatTopList = new ArrayList<>();

    @Override
    public RecentChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecentChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_message, parent, false));
    }

    public void setOnChatItemHandleListner(OnChatingHandlerListener onChatItemHandleListner) {
        this.onChatItemHandleListner = onChatItemHandleListner;
    }

    @Override
    public void onBindViewHolder(RecentChatViewHolder viewHolder, int position) {
        ChatItem chatItem = dataList.get(position);
        MessageSource source = chatItem.source;
        Message message = chatItem.message;
        viewHolder.itemView.setTag(source);
        viewHolder.itemView.setTag(R.id.message, chatItem);
        viewHolder.itemView.setOnClickListener(this);
        viewHolder.itemView.setOnLongClickListener(this);
        //显示消息来源名字
        viewHolder.name.setText(source.getTitle());
        viewHolder.name.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(), source.getTitleColor()));
        //显示消息来源头像
        if (source.getWebIcon() == null) {
            viewHolder.icon.setImageResource(source.getDefaultIconRID());
        } else {
            viewHolder.icon.load(source.getWebIcon(), source.getDefaultIconRID());
        }

        long noReadSum = MessageRepository.countNewIncludedTypesBySender(source.getId(), source.getMessageAction());
        viewHolder.badge.setText(String.valueOf(noReadSum));
        viewHolder.badge.setVisibility(noReadSum > 0 ? View.VISIBLE : View.GONE);


        viewHolder.preview.setCompoundDrawables(null, null, null, null);
        if (Constant.MessageStatus.STATUS_SENDING.equals(message.status)) {
            Drawable image = ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.item_msg_state_sending);
            image.setBounds(0, 0, (int) (Resources.getSystem().getDisplayMetrics().density * 18), (int) (Resources.getSystem().getDisplayMetrics().density * 18));
            viewHolder.preview.setCompoundDrawables(image, null, null, null);
        }

        if (Constant.MessageStatus.STATUS_SEND_FAILURE.equals(message.status)) {
            Drawable image = ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.item_msg_state_fail);
            image.setBounds(0, 0, (int) (Resources.getSystem().getDisplayMetrics().density * 15), (int) (Resources.getSystem().getDisplayMetrics().density * 15));
            viewHolder.preview.setCompoundDrawables(image, null, null, null);
        }
        viewHolder.itemView.setBackgroundResource(hasChatTop(chatItem) ? R.drawable.chat_top_list_background : R.drawable.background_bottom_line_selector);
        viewHolder.time.setText(AppTools.howTimeAgo(message.timestamp));
        String draft = Global.getLastChatDraft(source);
        if (TextUtils.isEmpty(draft)) {
            viewHolder.preview.setText(MessageParserFactory.getFactory().getMessageParser(message.action).getMessagePreviewText(message));
        } else {
            viewHolder.preview.setText(Html.fromHtml( viewHolder.itemView.getContext().getString(R.string.label_chat_draft, draft)));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addAll(List<ChatItem> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        dataList.clear();
        this.dataList.addAll(list);
        chatTopList.addAll(ChatTopRepository.queryList());
        Collections.sort(dataList, this);
        super.notifyDataSetChanged();
    }


    public boolean hasChatTop(ChatItem chat) {
        return indexOfChatTop(chat) >= 0;
    }

    public void cancelChatTop(ChatItem chatItem) {
        int index = indexOfChatTop(chatItem);
        chatTopList.remove(index);
        ChatTopRepository.delete(chatItem.getSourceClass(), chatItem.source.getId());
        int normalIndex = getNormalIndex(chatItem);
        dataList.remove(chatItem);
        dataList.add(normalIndex, chatItem);
        super.notifyItemMoved(index,normalIndex);
        super.notifyItemChanged(normalIndex);
    }

    /**
     * 当删除最后一条聊天记录是，在主页列表页面需要重新排序
     * @param sourceItem
     * @param currIndex
     */
    private void moveToOrderPosition(ChatItem sourceItem,int currIndex){
        int normalIndex = getNormalIndex(sourceItem);
        if (currIndex == normalIndex)
        {
            super.notifyItemChanged(currIndex);
            return;
        }

        dataList.remove(sourceItem);
        dataList.add(normalIndex, sourceItem);
        super.notifyItemMoved(currIndex,normalIndex);
        super.notifyItemChanged(normalIndex);

        /*
         当置顶的2个消息位置交换后，chatTopList也需要跟着变动
         */
        int srcTopIndex = indexOfChatTop(sourceItem);
        if (srcTopIndex > -1 && normalIndex <= chatTopList.size())
        {
            ChatTop srcTop = chatTopList.get(srcTopIndex);
            chatTopList.remove(srcTopIndex);
            chatTopList.add(normalIndex,srcTop);
            ChatTopRepository.updateSort(srcTop);
        }
    }


    /**
     * 发起新的聊天或者最新回复聊天，在主页列表需要移动到最顶部显示
     * @param chatItem
     */
    public void notifyItemMovedTop(ChatItem chatItem) {
        int topIndex = indexOfChatTop(chatItem);
        if (topIndex == -1) {
            notifyItemMoved(chatItem, chatTopList.size());
            return;
        }

        ChatTop srcTop = chatTopList.get(topIndex);
        chatTopList.remove(srcTop);
        chatTopList.add(0,srcTop);
        ChatTopRepository.updateSort(srcTop);

        notifyItemMoved(chatItem, 0);

    }

    public void notifyItemMovedTop(ChatTop top, ChatItem chatItem) {
        chatTopList.remove(top);
        chatTopList.add(0, top);
        ChatTopRepository.updateSort(top);
        notifyItemMoved(chatItem, 0);
    }

    private void notifyItemMoved(ChatItem chatItem, int toPosition) {
        int index = dataList.indexOf(chatItem);

        if (index > 0 && index != toPosition) {

            dataList.remove(index);
            dataList.add(toPosition, chatItem);
            super.notifyItemMoved(index,toPosition);
            super.notifyItemChanged(toPosition);
            return;
        }
        if (index > 0 && index == toPosition) {

            dataList.set(toPosition, chatItem);
            super.notifyItemChanged(toPosition);
            return;
        }

        if (index == 0) {
            dataList.set(toPosition, chatItem);
            super.notifyItemChanged(toPosition);
            return;
        }

        if (index < 0 && chatItem.message != null) {
            dataList.add(toPosition, chatItem);
            super.notifyItemInserted(toPosition);
        }


    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public boolean isEmpty() {
        return dataList.isEmpty();
    }


    @Override
    public boolean onLongClick(View v) {
        onChatItemHandleListner.onChatLongClicked((ChatItem) v.getTag(R.id.message));
        return true;
    }

    @Override
    public void onClick(View v) {
        onChatItemHandleListner.onChatClicked(((ChatItem) v.getTag(R.id.message)).source);
    }

    public void notifyItemRemoved(ChatItem target) {
        int index = dataList.indexOf(target);
        if (index >= 0) {
            dataList.remove(target);
            notifyItemRemoved(index);
        }
        index = indexOfChatTop(target);
        if (index >= 0) {
            chatTopList.remove(index);
            ChatTopRepository.delete(target.getSourceClass(), target.source.getId());
        }
    }

    /**
     * 关闭对话页面时，判断是否需要更新主页列表
     * @param target
     */
    public void notifyItemChanged(ChatItem target) {
        int index = dataList.indexOf(target);
        if (index==-1)
        {
            notifyItemMovedTop(target);
            return;
        }

        dataList.set(index, target);
        //如果是最新的聊天记录，显示在顶部，否则重新排序到应该显示的位置
        if (isLastMessage(target)) {
            notifyItemMovedTop(target);
        } else {
            moveToOrderPosition(target,index);
        }
    }

    private int indexOfChatTop(ChatItem srcChat) {
        int count = chatTopList.size();
        for (int i = 0; i < count; i++) {
            ChatTop top = chatTopList.get(i);
            if (Objects.equals(top.sourceName,srcChat.getSourceClass().getName()) && Objects.equals(top.sender,srcChat.source.getId())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 仅仅是刷新当前item的头像和名字，不涉及位置变换，比如收到用户头像更新通知的时候
     * @param item
     */
    public void notifyItemChangedOnly(ChatItem item) {
        int index = dataList.indexOf(item);
        if (index >= 0)
        {
            dataList.get(index).source = item.source;
            super.notifyItemChanged(index);
        }
    }
    /**
     * 返回按照规则，应该显示在列表的第几个位置
     * @param chatItem
     * @return
     */
    private int getNormalIndex(ChatItem chatItem) {
        List<Long> timeList = new ArrayList<>(dataList.size());
        for (ChatItem chat : dataList) {
            if (!hasChatTop(chat)) {
                timeList.add(chat.message.timestamp);
            }
        }
        Collections.sort(timeList, new TimeDescComparator());
        int index = timeList.indexOf(chatItem.message.timestamp);

        return index + chatTopList.size();
    }

    @Override
    public int compare(ChatItem arg0, ChatItem arg1) {

        int index0 = indexOfChatTop(arg0);
        int index1 = indexOfChatTop(arg1);
        if (index0 >= 0 && index1 >= 0) {
            return (int) (chatTopList.get(index1).sort - chatTopList.get(index0).sort);
        }
        if (index0 >= 0 && index1 < 0) {
            return -1;
        }
        if (index1 >= 0 && index0 < 0) {
            return 1;
        }
        return (int) (arg1.message.timestamp - arg0.message.timestamp);
    }


    public boolean isLastMessage(ChatItem item) {
        for (ChatItem chat : dataList) {
            if (chat.getLastTime() > item.getLastTime()) {
                return false;
            }
        }
        return true;
    }

}
