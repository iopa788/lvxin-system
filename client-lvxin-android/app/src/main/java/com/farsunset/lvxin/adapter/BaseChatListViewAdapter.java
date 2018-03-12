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

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.comparator.ChatRecordTimeAscComparator;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.listener.OnMessageDeleteListener;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 普通聊天页面的和公众号对话页面消息列表适配，主要实现消息时间显示的逻辑处理
 */
public abstract class BaseChatListViewAdapter <T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> implements OnMessageDeleteListener {
    protected List<Object> dataList = new ArrayList<>();
    protected User self;
    protected MessageSource others;
    protected static final int TYPE_DATE_TIME = -1;
    public BaseChatListViewAdapter(MessageSource friend) {
        super();
        self = Global.getCurrentUser();
        others = friend;
    }

    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    @Override
    public void onBindViewHolder(T viewHolder, int position) {

        Object item = dataList.get(position);
        if(item instanceof Long){
            onBindDateTimeViewHolder(viewHolder, (Long) item);
            return;
        }
        onBindMessageViewHolder(viewHolder, (Message) item);
    }

    public abstract void onBindDateTimeViewHolder(T viewHolder,long time);

    public abstract void onBindMessageViewHolder(T viewHolder,Message message);

    @Override
    public void onDelete(Message msg) {
        MessageRepository.deleteById(msg.mid);
        int index = dataList.indexOf(msg);
        /*
          刪除记录时，也要删除记录上面的时间，如果有的话
         */
        if (index > 0 && dataList.get(index-1) instanceof Long)
        {
            dataList.remove(index);
            dataList.remove(index-1);
            notifyItemRemoved(index);
            notifyItemRemoved(index-1);
        }else {
            dataList.remove(index);
            notifyItemRemoved(index);
        }
    }



    public void addMessage(Message message) {
        //如果是第一个消息，在消息上面显示时间
        if (dataList.isEmpty())
        {
            dataList.add(message.timestamp);
            dataList.add(message);
            notifyItemRangeInserted(0,2);
            return;
        }

        long lastTime =((Message) dataList.get(dataList.size()-1)).timestamp;
        long currTime =message.timestamp;
        //如果最新消息时间比上一个消息间隔2分钟以上，则在最新消息上显示时间
        if(currTime - lastTime >= Constant.CHATTING_TIME_SPACE){
            dataList.add(currTime);
            dataList.add(message);
            notifyItemRangeInserted(dataList.size()-2,2);
        }else {
            dataList.add(message);
            notifyItemInserted(dataList.size() - 1);
        }
    }

    public void addAllMessage(List<Message> list) {
        Collections.sort(list, new ChatRecordTimeAscComparator());
        List<Object> subList = new ArrayList<>();
        for (Message message :list)
        {
            //上一页的消息第一条显示时间
            if (subList.isEmpty())
            {
                subList.add(message.timestamp);
                subList.add(message);
                continue;
            }

            long lastTime =((Message) subList.get(subList.size()-1)).timestamp;
            long currTime =message.timestamp;
            //如果最新消息时间比上一个消息间隔2分钟以上，则在最新消息上显示时间
            if(currTime - lastTime >= Constant.CHATTING_TIME_SPACE){
                subList.add(currTime);
            }
            subList.add(message);
        }
        this.dataList.addAll(0, subList);
        notifyItemRangeInserted(0, subList.size());
    }

    public Message getLastMessage() {
        return dataList.isEmpty() ? null : (Message) dataList.get(dataList.size() - 1);
    }

    public  boolean contains(Message message){
        return dataList.contains(message);
    }
}
