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
package com.farsunset.lvxin.message.request;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.database.GroupRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.message.builder.Action102Builder;
import com.farsunset.lvxin.message.builder.Action103Builder;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.model.SystemMessage;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.SendMessageRequester;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.pro.R;
import com.google.gson.Gson;

public class Action102RequestHandler extends RequestHandler implements HttpRequestListener {

    private Action102Builder builder;

    @Override
    public void initialized(BaseActivity context, Message msg) {
        super.initialized(context, msg);
        builder = new Gson().fromJson(message.content, Action102Builder.class);
    }

    @Override
    public CharSequence getMessage() {
        StringBuffer buffer = new StringBuffer();
        Group target = GroupRepository.queryById(builder.groupId);
        buffer.append(context.getString(R.string.tip_request_joingroup, builder.name, target.name));

        SpannableString text = new SpannableString(buffer.toString());
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#3C568B")), 0, builder.name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new StyleSpan(Typeface.BOLD), 0, builder.name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    @Override
    public String getTitle() {
        return LvxinApplication.getInstance().getString(R.string.title_joingroup_request);
    }

    @Override
    public MessageSource decodeMessageSource() {
        Friend friend = new Friend();
        friend.account = builder.account;
        friend.name = builder.name;
        return friend;
    }

    @Override
    public void handleRefuse() {

        Message message = new Message();
        message.receiver = builder.account;
        message.content = context.getString(R.string.tip_refuse_joingroup, self.name, builder.groupName);
        message.action = Constant.MessageAction.ACTION_2;
        message.format = Constant.MessageFormat.FORMAT_TEXT;

        SendMessageRequester.send(message, null);

        MessageRepository.updateHandleStatus(SystemMessage.RESULT_REFUSE, message.mid);
        context.showToastView(context.getString(R.string.tip_handle_succeed));
        context.finish();
    }


    @Override
    public void handleAgree() {
        performAgreeJoinRequest();
    }

    private void performAgreeJoinRequest() {
        context.showProgressDialog(context.getString(R.string.tip_loading, context.getString(R.string.common_handle)));
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUPMEMBER_ADD_URL, BaseResult.class);
        requestBody.addParameter("groupId", builder.groupId);
        requestBody.addParameter("account", builder.account);
        HttpRequestLauncher.execute(requestBody, this);
    }


    @Override
    public CharSequence getDescription() {
        return context.getString(R.string.tip_request_verify, builder.requestMsg);
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {
        context.hideProgressDialog();
        if (result.isSuccess() && URLConstant.GROUPMEMBER_ADD_URL.equals(url)) {
            MessageRepository.batchModifyAgree(builder.account, message.action);
            context.showToastView(context.getString(R.string.tip_handle_succeed));

            sendAgreeMessageQuitly();
            context.finish();
        }
    }


    private void sendAgreeMessageQuitly() {
        Message message = new Message();
        message.receiver = builder.account;
        message.sender = Constant.SYSTEM;
        message.action = Constant.MessageAction.ACTION_103;
        message.content = new Action103Builder().buildJsonString(self, GroupRepository.queryById(builder.groupId));
        message.format = Constant.MessageFormat.FORMAT_TEXT;
        SendMessageRequester.send(message, null);
    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        context.hideProgressDialog();
    }


}
