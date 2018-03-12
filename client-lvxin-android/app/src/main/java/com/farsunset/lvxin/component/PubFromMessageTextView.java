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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.farsunset.lvxin.activity.contact.MessageForwardActivity;
import com.farsunset.lvxin.activity.contact.PubAccountDetailActivity;
import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.network.model.PubTextMessage;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.farsunset.lvxin.util.MessageUtil;
import com.google.gson.Gson;

public class PubFromMessageTextView extends PubFromMessageView implements OnClickListener  {
    private WebImageView logo;
    private EmoticonTextView textView;

    public PubFromMessageTextView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        logo = (WebImageView) findViewById(R.id.logo);
        logo.setOnClickListener(this);
        textView = (EmoticonTextView) findViewById(R.id.textview);
        int padding = textView.getPaddingLeft() + textView.getPaddingRight();
        textView.setMaxWidth(padding + Global.getChatTextMaxWidth());
    }

    @Override
    public void displayMessage() {
        textView.setOnLongClickListener(this);
        logo.load(FileURLBuilder.getPubAccountLogoUrl(others.getId()), others.getDefaultIconRID());
        PubTextMessage textMessage = new Gson().fromJson(message.content, PubTextMessage.class);
        textView.setFaceSize(Constant.EMOTION_FACE_SIZE);
        textView.setClickable(false);
        textView.setText(textMessage.content);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logo) {
            Intent intent = new Intent(getContext(), PubAccountDetailActivity.class);
            intent.putExtra(PublicAccount.NAME, others);
            getContext().startActivity(intent);
        }

    }

    @Override
    public void onMenuItemClicked(int id) {

        super.onMenuItemClicked(id);

        if (id == R.id.menu_copy) {
            ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText(null, textView.getText().toString()));
            Snackbar.make(this, R.string.tip_copy_successed, Snackbar.LENGTH_SHORT).show();
        }

        if (id == R.id.menu_forward) {
            Message target = MessageUtil.clone(message);
            target.content = textView.getText().toString();
            Intent intent = new Intent(getContext(), MessageForwardActivity.class);
            intent.putExtra(Message.NAME, target);
            getContext().startActivity(intent);
        }
    }


    @Override
    public Object getTag() {
        return logo.getTag(R.id.logo);
    }


    @Override
    public void setTag(Object obj) {
        logo.setTag(R.id.logo, obj);
    }

}
