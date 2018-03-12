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
package com.farsunset.lvxin.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.farsunset.lvxin.activity.chat.MMWebViewActivity;
import com.farsunset.lvxin.activity.contact.MessageForwardActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.network.model.PubLinkMessage;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.MessageUtil;
import com.google.gson.Gson;

public class PubFromMessageLinkView extends PubFromMessageView implements OnClickListener {
    private PubLinkMessage linkMsg;
    private TextView title;
    private TextView descrpiton;
    private WebImageView banner;

    public PubFromMessageLinkView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        title = (TextView) findViewById(R.id.title);
        descrpiton = (TextView) findViewById(R.id.descrpiton);
        banner = (WebImageView) findViewById(R.id.banner);
    }

    @Override
    public void displayMessage() {
        findViewById(R.id.linkPanelView).setOnLongClickListener(this);
        linkMsg = new Gson().fromJson(super.message.content, PubLinkMessage.class);
        title.setText(linkMsg.title);
        descrpiton.setText(linkMsg.content);
        banner.setImageResource(R.color.theme_window_color);
        banner.load(linkMsg.image);
        findViewById(R.id.linkPanelView).setOnClickListener(this);
    }


    @Override
    public void onMenuItemClicked(int id) {

        super.onMenuItemClicked(id);

        if (id == R.id.menu_forward) {
            Message target = MessageUtil.clone(message);
            target.content = linkMsg.toString();
            target.format = Constant.MessageFormat.FORMAT_TEXT;
            Intent intent = new Intent(getContext(), MessageForwardActivity.class);
            intent.putExtra(Message.NAME, target);
            getContext().startActivity(intent);
        }
    }

    @Override
    public Object getTag() {
        return findViewById(R.id.linkPanelView).getTag(R.id.logo);
    }


    @Override
    public void setTag(Object obj) {
        findViewById(R.id.linkPanelView).setTag(R.id.logo, obj);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), MMWebViewActivity.class);
        intent.setData(Uri.parse(linkMsg.link));
        getContext().startActivity(intent);
    }

}
