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
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.database.GroupMemberRepository;
import com.farsunset.lvxin.database.GroupRepository;
import com.farsunset.lvxin.database.MessageRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.message.builder.Action105Builder;
import com.farsunset.lvxin.message.builder.Action106Builder;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.model.SystemMessage;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.SendMessageRequester;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.GroupMemberListResult;
import com.farsunset.lvxin.pro.R;
import com.google.gson.Gson;


public class Action105RequestHandler extends RequestHandler implements HttpRequestListener {

    Action105Builder builder;

    @Override
    public void initialized(BaseActivity context, Message msg) {
        super.initialized(context, msg);
        builder = new Gson().fromJson(message.content, Action105Builder.class);
    }

    @Override
    public CharSequence getMessage() {

        SpannableString text = new SpannableString(context.getString(R.string.tip_request_invitegroup, builder.name, builder.groupName));
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#3C568B")), 0, builder.name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new StyleSpan(Typeface.BOLD), 0, builder.name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    @Override
    public String getTitle() {
        return context.getString(R.string.tip_title_groupmessage);
    }

    @Override
    public MessageSource decodeMessageSource() {
        return decodeMessageBody();
    }

    @Override
    public void handleRefuse() {

        Message message = new Message();
        message.receiver = builder.account;
        message.content = context.getString(R.string.tip_refuse_invitegroup, self.name, builder.groupName);
        message.action = Constant.MessageAction.ACTION_2;
        message.format = Constant.MessageFormat.FORMAT_TEXT;

        SendMessageRequester.send(message, null);

        MessageRepository.updateHandleStatus(SystemMessage.RESULT_REFUSE, message.mid);

        context.showToastView(context.getString(R.string.tip_handle_succeed));
        context.finish();
    }


    private void performAgreeJoinRequest() {
        context.showProgressDialog(context.getString(R.string.tip_loading, context.getString(R.string.common_handle)));
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUPMEMBER_ADD_URL, BaseResult.class);
        requestBody.addParameter("groupId", builder.groupId);
        requestBody.addParameter("account", self.account);
        HttpRequestLauncher.execute(requestBody, this);
    }

    @Override
    public void handleAgree() {
        performAgreeJoinRequest();
    }

    @Override
    public CharSequence getDescription() {
        return builder.groupSummary;
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {

        if (result.isSuccess() && url.equals(URLConstant.GROUPMEMBER_ADD_URL)) {

            MessageRepository.batchModifyAgree(builder.account, message.action);
            context.showToastView(context.getString(R.string.tip_handle_succeed));
            GroupRepository.add(decodeMessageBody());
            sendAgreeMessageQuitly();

            performLoadMembersRequest();

        }

        if (result.isSuccess() && url.equals(URLConstant.GROUPMEMBER_LIST_URL)) {

            GroupMemberRepository.saveAll(((GroupMemberListResult) result).dataList);
            context.hideProgressDialog();
            context.finish();
        }

        if (result.code.equals(Constant.ReturnCode.CODE_404)) {
            context.showToastView(R.string.tip_group_has_dissolved);
        }

    }

    private void performLoadMembersRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUPMEMBER_LIST_URL, GroupMemberListResult.class);
        requestBody.addParameter("groupId", builder.groupId);
        HttpRequestLauncher.execute(requestBody, this);
    }


    private void sendAgreeMessageQuitly() {
        Message message = new Message();
        message.receiver = builder.account;
        message.sender = Constant.SYSTEM;
        message.action = Constant.MessageAction.ACTION_106;
        message.content = new Action106Builder().buildJsonString(self, decodeMessageBody());
        message.format = Constant.MessageFormat.FORMAT_TEXT;
        SendMessageRequester.send(message, null);
    }


    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        context.hideProgressDialog();
    }

    public Group decodeMessageBody() {
        Group group = new Group();
        group.groupId = builder.groupId;
        group.name = builder.groupName;
        group.founder = builder.groupFounder;
        group.summary = builder.groupSummary;
        group.category = builder.groupCategory;
        return group;
    }
}
