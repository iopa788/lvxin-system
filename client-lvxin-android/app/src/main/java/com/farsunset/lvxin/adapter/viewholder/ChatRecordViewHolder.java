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
package com.farsunset.lvxin.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.farsunset.lvxin.component.BaseFromMessageView;
import com.farsunset.lvxin.component.BaseToMessageView;
import com.farsunset.lvxin.pro.R;

public class ChatRecordViewHolder extends RecyclerView.ViewHolder {
    public BaseToMessageView toMessageView;
    public BaseFromMessageView fromMessageView;
    public TextView dateTime;

    public ChatRecordViewHolder(View itemMessageView) {
        super(itemMessageView);
        dateTime  = (TextView)itemMessageView.findViewById(R.id.datetime);
        toMessageView = (BaseToMessageView) itemMessageView.findViewById(R.id.to_message_view);
        fromMessageView = (BaseFromMessageView) itemMessageView.findViewById(R.id.from_message_view);
    }
}
