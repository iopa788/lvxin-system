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

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.network.MessageForwardHandler;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.pro.R;
import com.google.gson.Gson;

public class MessageSharedActivity extends MessageForwardActivity {

    private Uri mImageUri;

    @Override
    public void initComponents() {
        User user = Global.getCurrentUser();
        if (user == null || user.password == null)
        {
            LvxinApplication.getInstance().restartSelf();
            finish();
            return;
        }
        mImageUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        super.initComponents();
    }

    @Override
    public Message getMessage() {

        Message message = new Message();
        message.sender = self.account;

        if (mImageUri != null) {
            SNSImage chatImage = new SNSImage();
            chatImage.image = mImageUri.toString();
            message.content = new Gson().toJson(chatImage);
            message.format = Constant.MessageFormat.FORMAT_IMAGE;
        }else {
            String text = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            String title = getIntent().getStringExtra(Intent.EXTRA_SUBJECT);
            message.content = TextUtils.isEmpty(title) ? text : title + "\n" + text;
            message.format = Constant.MessageFormat.FORMAT_TEXT;
        }
        return message;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_share_to_contact;
    }

    @Override
    public void onRightButtonClicked() {
        sharedDialog.dismiss();
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_send)));
        MessageForwardHandler.forward(message, mImageUri, this, reciverList);
    }

}
