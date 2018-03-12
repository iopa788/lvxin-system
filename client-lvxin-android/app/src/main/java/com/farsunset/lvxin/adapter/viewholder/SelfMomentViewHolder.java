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
import android.widget.GridLayout;
import android.widget.TextView;

import com.farsunset.lvxin.component.SNSImageView;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.pro.R;


public class SelfMomentViewHolder extends RecyclerView.ViewHolder {
    public int viewType;
    public TextView text;
    public SNSImageView singleImageView;
    public GridLayout gridImageLayout;
    public View linkPanel;
    public TextView linkTitle;
    public TextView day;
    public TextView month;
    public WebImageView thumbnailView;

    public SelfMomentViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
        thumbnailView = (WebImageView) itemView.findViewById(R.id.thumbnailView);
        singleImageView = (SNSImageView) itemView.findViewById(R.id.singleImageView);
        gridImageLayout = (GridLayout) itemView.findViewById(R.id.imageGridLayout);
        day = (TextView) itemView.findViewById(R.id.day);
        month = (TextView) itemView.findViewById(R.id.month);
        text = (TextView) itemView.findViewById(R.id.text);
        linkTitle = (TextView) itemView.findViewById(R.id.linkTitle);
        linkPanel = itemView.findViewById(R.id.linkPanel);
    }
}
