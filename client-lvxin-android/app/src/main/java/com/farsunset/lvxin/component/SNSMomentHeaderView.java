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
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.farsunset.lvxin.activity.trend.MomentMessageActivity;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.model.Comment;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.google.gson.Gson;

import java.util.List;

public class SNSMomentHeaderView extends LinearLayout implements View.OnClickListener {
    private OnItemClickedListener onIconClickedListener;

    public SNSMomentHeaderView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.sns_message_remind).setOnClickListener(this);
        findViewById(R.id.iconCardView).setOnClickListener(this);
    }

    public void showMessageRemind(List<Message> msgList) {
        if (!msgList.isEmpty()) {
            findViewById(R.id.sns_message_remind).setVisibility(View.VISIBLE);
            for (int i = 0; i < msgList.size(); i++) {
                Comment comment = new Gson().fromJson(msgList.get(i).content, Comment.class);
                if (i == 0) {
                    ((WebImageView) findViewById(R.id.sns_notify_firstimg)).load(FileURLBuilder.getUserIconUrl(comment.account), R.drawable.icon_def_head);
                }
                if (i == 1) {
                    ((WebImageView) findViewById(R.id.sns_notify_secondimg)).load(FileURLBuilder.getUserIconUrl(comment.account), R.drawable.icon_def_head);
                }
                if (i == 2) {
                    ((WebImageView) findViewById(R.id.sns_notify_lastimg)).load(FileURLBuilder.getUserIconUrl(comment.account), R.drawable.icon_def_head);
                }
            }
        } else {
            findViewById(R.id.sns_message_remind).setVisibility(View.GONE);
        }
    }

    public void displayIcon(String url) {
        ((WebImageView) findViewById(R.id.icon)).load(url, R.drawable.icon_def_head);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sns_message_remind:
                Intent intent = new Intent(getContext(), MomentMessageActivity.class);
                getContext().startActivity(intent);
                findViewById(R.id.sns_message_remind).setVisibility(View.GONE);
                break;
            case R.id.iconCardView:
                if (onIconClickedListener != null) {
                    onIconClickedListener.onItemClicked(view.getId(), view);
                }
                break;
        }
    }

    public void setOnIconClickedListener(OnItemClickedListener onIconClickedListener) {
        this.onIconClickedListener = onIconClickedListener;
    }


}
