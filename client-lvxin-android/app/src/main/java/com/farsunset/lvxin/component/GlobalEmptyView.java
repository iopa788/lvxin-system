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
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.farsunset.lvxin.pro.R;


public class GlobalEmptyView extends FrameLayout {
    private TextView tips;
    private ImageView icon;

    public GlobalEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_view, this);
        tips = (TextView) findViewById(R.id.tips);
        icon = (ImageView) findViewById(R.id.icon);

    }


    public void setTips(int sid) {
        tips.setText(sid);
    }

    public void setIcon(int resId) {
        icon.setImageResource(resId);
    }

    public void toggle(RecyclerView.Adapter adapter) {
        setVisibility(adapter.getItemCount() > 0 ? GONE : VISIBLE);
    }
}
