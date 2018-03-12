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
import android.view.View;
import android.widget.TextView;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.message.request.RequestHandler;
import com.farsunset.lvxin.message.request.RequestHandlerFactory;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.pro.R;

/**
 * 处理请求好友 ， 请求 入群
 */
public class RequestHandleActivity extends BaseActivity {

    private RequestHandler messageHandler;

    private MessageSource source; //谁请求

    @Override
    public void initComponents() {


        findViewById(R.id.ignoreButton).setOnClickListener(this);
        findViewById(R.id.agreeButton).setOnClickListener(this);
        findViewById(R.id.refuseButton).setOnClickListener(this);

        Message message = (Message) this.getIntent().getExtras().getSerializable("message");
        messageHandler = RequestHandlerFactory.getFactory().getMessageHandler(message.action);
        messageHandler.initialized(this, message);
        source = messageHandler.decodeMessageSource();
        setTitle(messageHandler.getTitle());
        ((TextView) findViewById(R.id.messsage)).setText(messageHandler.getMessage());
        ((TextView) findViewById(R.id.description)).setText(messageHandler.getDescription());

        ((TextView) findViewById(R.id.name)).setText(source.getName());
        WebImageView icon = ((WebImageView) findViewById(R.id.icon));
        icon.load(source.getWebIcon(), source.getDefaultIconRID(), 999);
        icon.setOnClickListener(this);

        Intent intent = new Intent();
        intent.putExtra("mid", message.mid);
        setResult(RESULT_OK, intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.icon:
                if (source instanceof Friend) {
                    Intent intent = new Intent(this, UserDetailActivity.class);
                    intent.putExtra(Friend.class.getName(), source);
                    startActivity(intent);
                }
                break;

            case R.id.agreeButton:
                messageHandler.handleAgree();
                break;

            case R.id.ignoreButton:
                messageHandler.handleIgnore();
                break;

            case R.id.refuseButton:
                messageHandler.handleRefuse();
                break;

        }
    }


    @Override
    public int getContentLayout() {

        return R.layout.activity_request_handle;
    }


    @Override
    public int getToolbarTitle() {

        return 0;
    }

}
