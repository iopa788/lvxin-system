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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farsunset.lvxin.activity.chat.MMWebViewActivity;
import com.farsunset.lvxin.network.model.PubLinksMessage;
import com.farsunset.lvxin.pro.R;
import com.google.gson.Gson;

public class PubFromMessageLinkPanelView extends PubFromMessageView implements OnClickListener {
    private LinearLayout linkPanelView;

    public PubFromMessageLinkPanelView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        linkPanelView = (LinearLayout) findViewById(R.id.linksPanelView);
    }

    @Override
    public void displayMessage() {

        linkPanelView.setOnLongClickListener(this);
        PubLinksMessage linkMsg = new Gson().fromJson(super.message.content, PubLinksMessage.class);
        linkPanelView.removeAllViews();
        View topLine = LayoutInflater.from(getContext()).inflate(R.layout.layout_pubchat_linkbanner, null);
        ((TextView) topLine.findViewById(R.id.title)).setText(linkMsg.title);
        WebImageView image = ((WebImageView) topLine.findViewById(R.id.banner));
        image.setImageResource(R.color.theme_window_color);
        image.load(linkMsg.image);
        topLine.setTag(linkMsg.link);
        topLine.setOnClickListener(this);
        linkPanelView.addView(topLine);
        if (linkMsg.hasMore()) {
            topLine.setBackgroundResource(R.drawable.item_background_top);
            for (int i = 0; i < linkMsg.items.size(); i++) {
                View item = LayoutInflater.from(getContext()).inflate(R.layout.layout_pubchat_itemlink, null);
                ((TextView) item.findViewById(R.id.title)).setText(linkMsg.getSubLink(i).title);
                WebImageView iconImage = ((WebImageView) item.findViewById(R.id.image));
                iconImage.setImageResource(R.color.theme_window_color);
                iconImage.load(linkMsg.getSubLink(i).image);
                linkPanelView.addView(item);
                item.setTag(linkMsg.getSubLink(i).link);
                item.setOnClickListener(this);
                if (i == linkMsg.items.size()) {
                    item.setBackgroundResource(R.drawable.item_background_bottom);
                } else {
                    item.setBackgroundResource(R.drawable.item_background_middle);
                }
            }

        } else {
            topLine.setBackgroundResource(R.drawable.item_background_single);
        }
    }


    @Override
    public Object getTag() {
        return linkPanelView.getTag(R.id.logo);
    }


    @Override
    public void setTag(Object obj) {
        linkPanelView.setTag(R.id.logo, obj);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), MMWebViewActivity.class);
        intent.setData(Uri.parse(v.getTag().toString()));
        getContext().startActivity(intent);
    }

}
