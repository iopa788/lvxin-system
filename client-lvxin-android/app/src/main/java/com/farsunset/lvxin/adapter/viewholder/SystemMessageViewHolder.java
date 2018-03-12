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
import android.widget.Button;
import android.widget.TextView;

import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.pro.R;


public class SystemMessageViewHolder extends RecyclerView.ViewHolder {
    public TextView categoryText;
    public TextView time;
    public WebImageView icon;
    public TextView content;
    public Button handleButton;
    public TextView tipText;
    public View colorTitle;

    public SystemMessageViewHolder(View itemView) {
        super(itemView);
        categoryText = ((TextView) itemView.findViewById(R.id.categoryText));
        time = ((TextView) itemView.findViewById(R.id.time));
        icon = ((WebImageView) itemView.findViewById(R.id.icon));
        content = ((TextView) itemView.findViewById(R.id.content));
        colorTitle = itemView.findViewById(R.id.colorTitle);
        tipText = ((TextView) itemView.findViewById(R.id.tipText));
        handleButton = ((Button) itemView.findViewById(R.id.handleButton));
    }
}
