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
import android.widget.TextView;

import com.farsunset.lvxin.activity.chat.MMWebViewActivity;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.listener.OnCommentSelectedListener;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.model.PublishObject;
import com.farsunset.lvxin.pro.R;
import com.google.gson.Gson;

public class SnsItemMomentLinkView extends SnsItemMomentView {
    private TextView linkTitle;
    private String linkUrl;

    public SnsItemMomentLinkView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        linkTitle = (TextView) findViewById(R.id.linkTitle);
        ((View) linkTitle.getParent()).setOnClickListener(this);
    }

    @Override
    public void displayMoment(Moment moment, User self, OnCommentSelectedListener commentSelectedListener) {
        super.displayMoment(moment, self, commentSelectedListener);
        final PublishObject publishObject = new Gson().fromJson(moment.content, PublishObject.class);
        linkUrl = publishObject.link.link;
        linkTitle.setText(publishObject.link.title);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == linkTitle.getParent()) {
            Intent intent = new Intent(getContext(), MMWebViewActivity.class);
            intent.setData(Uri.parse(linkUrl));
            getContext().startActivity(intent);
        }
    }

}
