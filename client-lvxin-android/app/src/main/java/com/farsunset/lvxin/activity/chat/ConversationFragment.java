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
package com.farsunset.lvxin.activity.chat;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farsunset.lvxin.activity.HomeActivity;
import com.farsunset.lvxin.activity.base.CIMMonitorFragment;
import com.farsunset.lvxin.adapter.ConversationListViewAdapter;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.SkinManager;
import com.farsunset.lvxin.bean.ChatItem;
import com.farsunset.lvxin.database.ChatTopRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.dialog.ContentMenuWindow;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.listener.OnChatingHandlerListener;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.listener.OnMenuClickedListener;
import com.farsunset.lvxin.message.parser.MessageParserFactory;
import com.farsunset.lvxin.model.ChatTop;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.model.SystemMessage;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.MessageUtil;

import java.util.LinkedList;

public class ConversationFragment extends CIMMonitorFragment implements OnDialogButtonClickListener, OnChatingHandlerListener, OnMenuClickedListener {

    private static final LinkedList<String> INCLUDED_MESSAGE_TYPES = new LinkedList();

    static {
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_0);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_1);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_2);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_3);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_200);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_201);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_102);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_103);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_104);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_105);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_106);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_107);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_112);
    }

    ;

    private ContentMenuWindow contentMenuWindow;
    private ConversationListViewAdapter adapter;
    private RecyclerView conversationListView;
    private CustomDialog customDialog;
    private ChatBroadcastReceiver chatBroadcastReceiver;

    private int connCounter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LvxinApplication.getInstance().connectPushServer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_conversation, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        conversationListView = (RecyclerView) findViewById(R.id.conversationList);
        conversationListView.setItemAnimator(new DefaultItemAnimator());
        conversationListView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        adapter = new ConversationListViewAdapter();
        conversationListView.setAdapter(adapter);
        adapter.setOnChatItemHandleListner(this);
        customDialog = new CustomDialog(this.getActivity());
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setTitle((R.string.title_delete_message));
        customDialog.setMessage((R.string.tip_delete_message));
        contentMenuWindow = new ContentMenuWindow(this.getActivity());
        contentMenuWindow.setOnMenuClickedListener(this);

        loadRecentConversation();
    }

    public void onResume() {
        super.onResume();
        showNewMsgLable();
        ((NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
    }

    public void loadRecentConversation() {
        adapter.addAll(MessageRepository.getRecentMessage(INCLUDED_MESSAGE_TYPES.toArray()));
        toogleEmptyView();
    }

    public void toogleEmptyView() {
        findViewById(R.id.nochatbgimage).setVisibility(adapter.isEmpty() ? View.VISIBLE : View.GONE);
    }


    private void showNewMsgLable() {
        View tabView = ((HomeActivity) getActivity()).getConversationTab();
        TextView newMsgBadge = (TextView) tabView.findViewById(R.id.badge);
        int sum = MessageRepository.queryIncludedNewCount(INCLUDED_MESSAGE_TYPES.toArray());
        if (sum > 0) {
            newMsgBadge.setText(String.valueOf(sum));
            newMsgBadge.setVisibility(View.VISIBLE);
        } else {
            newMsgBadge.setVisibility(View.GONE);
        }
    }

    @Override
    public void onChatClicked(MessageSource source) {

        Intent intent = new Intent();

        if (source instanceof Group) {
            intent.setClass(getContext(), GroupChatActivity.class);
            intent.putExtra(Constant.CHAT_OTHRES_ID, ((Group) source).groupId);
            intent.putExtra(Constant.CHAT_OTHRES_NAME, ((Group) source).name);
        }

        if (source instanceof Friend) {
            intent.setClass(getContext(), FriendChatActivity.class);
            intent.putExtra(Constant.CHAT_OTHRES_ID, ((Friend) source).account);
            intent.putExtra(Constant.CHAT_OTHRES_NAME, ((Friend) source).name);
        }

        if (source instanceof PublicAccount) {
            intent.setClass(getContext(), PubAccountChatActivity.class);
            intent.putExtra(PublicAccount.NAME, source);
        }

        if (source instanceof SystemMessage) {
            intent.setClass(getContext(), SystemMessageActivity.class);
        }

        startActivity(intent);
    }


    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {
        if (INCLUDED_MESSAGE_TYPES.contains(message.getAction())) {
            Message msg = MessageUtil.transform(message);
            ChatItem chatItem = new ChatItem();
            chatItem.message = msg;
            chatItem.source = MessageParserFactory.getFactory().getMessageParser(msg.action).getMessageSource(msg);
            adapter.notifyItemMovedTop(chatItem);
            findViewById(R.id.nochatbgimage).setVisibility(View.GONE);

            showNewMsgLable();
        }
    }


    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        customDialog.dismiss();
        ChatItem chatItem = (ChatItem) customDialog.getTag();
        adapter.notifyItemRemoved(chatItem);
        String id = chatItem.source.getId();
        MessageRepository.deleteBySenderOrReceiver(id);
        Global.removeChatDraft(chatItem.source);
        toogleEmptyView();
        showNewMsgLable();
    }


    @Override
    public void onChatLongClicked(ChatItem chat) {
        TextView badge = (TextView) conversationListView.findViewWithTag(chat.source).findViewById(R.id.badge);
        customDialog.setTag(chat);
        contentMenuWindow.buildChatMenuGroup(adapter.hasChatTop(chat), badge.getVisibility() == View.VISIBLE);
        contentMenuWindow.show((View) badge.getParent());
    }

    @Override
    public void onMenuItemClicked(int id) {

        if (id == R.id.menu_delete_chat) {
            customDialog.show();
        }
        if (id == R.id.menu_mark_noread) {
            markMessageNoread((ChatItem) customDialog.getTag());
        }
        if (id == R.id.menu_mark_read) {
            markMessageRead((ChatItem) customDialog.getTag());
        }
        if (id == R.id.menu_chat_top) {
            moveChatTop((ChatItem) customDialog.getTag());
        }
        if (id == R.id.menu_cancel_top) {
            cancelChatTop((ChatItem) customDialog.getTag());
        }
    }

    /**
     * 置顶聊天
     *
     * @param chat
     */
    private void moveChatTop(ChatItem chat) {
        ChatTop top = ChatTopRepository.setTop(chat.getSourceClass(), chat.source.getId());
        adapter.notifyItemMovedTop(top, chat);
    }


    /**
     * 取消置顶聊天
     *
     * @param chat
     */
    private void cancelChatTop(ChatItem chat) {
        adapter.cancelChatTop(chat);
        conversationListView.scrollToPosition(0);
    }


    /**
     * 标记消息为未读
     *
     * @param chat
     */
    private void markMessageNoread(ChatItem chat) {
        Message message = MessageRepository.queryReceivedLastMessage(chat.source.getId(), chat.source.getMessageAction());
        TextView badge = (TextView) conversationListView.findViewWithTag(chat.source).findViewById(R.id.badge);
        if (badge.getVisibility() == View.GONE && message != null) {
            MessageRepository.updateStatus(message.mid, Message.STATUS_NOT_READ);
            badge.setVisibility(View.VISIBLE);
            badge.setText("1");
            showNewMsgLable();
        }

    }

    /**
     * 标记消息为已读
     *
     * @param chat
     */
    private void markMessageRead(ChatItem chat) {
        MessageRepository.batchReadMessage(chat.source.getId(), chat.source.getMessageAction());
        TextView badge = (TextView) conversationListView.findViewWithTag(chat.source).findViewById(R.id.badge);
        badge.setVisibility(View.GONE);
        badge.setText(null);
        showNewMsgLable();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        chatBroadcastReceiver = new ChatBroadcastReceiver();
        LvxinApplication.registerLocalReceiver(chatBroadcastReceiver, chatBroadcastReceiver.getIntentFilter());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LvxinApplication.unregisterLocalReceiver(chatBroadcastReceiver);
    }

    @Override
    public void onConnectionSuccessed(boolean hasAutoBind) {
        connCounter = 0;
        getActivity().setTheme(SkinManager.getSkinTheme());
    }

    @Override
    public void onConnectionFailed() {
        connCounter++;
        if (connCounter >= 3) {
            getActivity().setTheme(R.style.Error_Theme);
        }
    }

    public class ChatBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            ChatItem item = (ChatItem) intent.getSerializableExtra(ChatItem.NAME);

            /**
             * 如果是不需要再列表显示的消息，不处理
             */
            if (item.message != null && !INCLUDED_MESSAGE_TYPES.contains(item.message.action)) {
                return;
            }
            if (Constant.Action.ACTION_RECENT_APPEND_CHAT.equals(intent.getAction())) {
                adapter.notifyItemMovedTop(item);
                toogleEmptyView();
            }

            if (Constant.Action.ACTION_RECENT_DELETE_CHAT.equals(intent.getAction())) {
                adapter.notifyItemRemoved(item);
                toogleEmptyView();
            }

            if (Constant.Action.ACTION_RECENT_REFRESH_CHAT.equals(intent.getAction())) {
                adapter.notifyItemChanged(item);
                toogleEmptyView();
            }
            /**
             * 收到刷新logo或者名称的通知
             */
            if (Constant.Action.ACTION_RECENT_REFRESH_LOGO.equals(intent.getAction())) {
                adapter.notifyItemChangedOnly(item);
            }
        }

        public IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_RECENT_APPEND_CHAT);
            filter.addAction(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            filter.addAction(Constant.Action.ACTION_RECENT_REFRESH_CHAT);
            filter.addAction(Constant.Action.ACTION_RECENT_REFRESH_LOGO);

            return filter;
        }
    }


}
