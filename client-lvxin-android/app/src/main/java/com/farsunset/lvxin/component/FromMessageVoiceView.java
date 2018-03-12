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
import android.util.AttributeSet;
import android.view.View;

import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.pro.R;

public class FromMessageVoiceView extends BaseFromMessageView {
    protected View readDot;

    public FromMessageVoiceView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public View getContentView() {
        return voiceView;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        readDot = findViewById(R.id.voiceReadDot);
    }

    @Override
    public void displayMessage() {
        voiceView.display(message, false);
        voiceView.setOnClickListener(voiceView);

        if (!Message.STATUS_READ_OF_VOICE.equals(message.status)) {
            readDot.setVisibility(View.VISIBLE);
        } else {
            readDot.setVisibility(View.INVISIBLE);
        }
    }

    public void hideReadDot() {
        readDot.setVisibility(View.INVISIBLE);
    }


}
